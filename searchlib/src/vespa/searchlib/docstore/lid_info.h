// Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.

#pragma once

#include <vespa/vespalib/util/generationhandler.h>
#include <vespa/vespalib/util/sync.h>
#include <limits>
#include <vector>

namespace search {

class LidInfo {
public:
    LidInfo() : _value() { }
    LidInfo(uint64_t rep) { _value.r = rep; }
    LidInfo(uint32_t fileId, uint32_t chunkId, uint32_t size);
    uint32_t getFileId()  const { return _value.v.fileId; }
    uint32_t getChunkId() const { return _value.v.chunkId; }
    uint32_t size()       const { return _value.v.size << SIZE_SHIFT; }
    operator uint64_t ()  const { return _value.r; }
    bool empty()          const { return size() == 0; }
    bool valid() const { return _value.r != std::numeric_limits<uint64_t>::max(); }

    bool operator==(const LidInfo &b) const {
        return (getFileId() == b.getFileId()) &&
               (getChunkId() == b.getChunkId());
    }
    bool operator < (const LidInfo &b) const {
        return (getFileId() == b.getFileId())
                   ? (getChunkId() < b.getChunkId())
                   : (getFileId() < b.getFileId());
    }
    static uint32_t getFileIdLimit() { return 1 << NUM_FILE_BITS; }
    static uint32_t getChunkIdLimit() { return 1 << NUM_CHUNK_BITS; }
private:
    static uint32_t computeAlignedSize(uint32_t sz) {
        return (sz+((1<<SIZE_SHIFT)-1)) >> SIZE_SHIFT;
    }
    static uint32_t getSizeLimit() {
        return std::numeric_limits<uint32_t>::max() - ((2<<SIZE_SHIFT)-2);
    }
    static constexpr uint32_t NUM_FILE_BITS = 16;
    static constexpr uint32_t NUM_CHUNK_BITS = 22;
    static constexpr uint32_t NUM_SIZE_BITS = 26;
    static constexpr uint32_t SIZE_SHIFT = 32 - NUM_SIZE_BITS;
    struct Rep {
        uint64_t fileId : NUM_FILE_BITS;
        uint64_t chunkId : NUM_CHUNK_BITS;
        uint64_t size : NUM_SIZE_BITS;
    };
    union Value {
        Value() : r(std::numeric_limits<uint64_t>::max()) { }
        Rep v;
        uint64_t r;
    } _value;
};

class LidInfoWithLid : public LidInfo {
public:
    LidInfoWithLid(LidInfo lidInfo, uint32_t lid) : LidInfo(lidInfo), _lid(lid) { }
    uint32_t getLid() const { return _lid; }
private:
    uint32_t _lid;
};

typedef std::vector<LidInfoWithLid> LidInfoWithLidV;

class ISetLid
{
public:
    using LockGuard = vespalib::LockGuard;
    virtual ~ISetLid() { }
    virtual void setLid(const LockGuard & guard, uint32_t lid, const LidInfo & lm) = 0;
};

class IGetLid
{
public:
    using Guard = vespalib::GenerationHandler::Guard;
    virtual ~IGetLid() { }

    virtual LidInfo getLid(Guard & guard, uint32_t lid) const = 0;
    virtual vespalib::LockGuard getLidGuard(uint32_t lid) const = 0;
    virtual Guard getLidReadGuard() const = 0;
};

}
