# Copyright 2017 Yahoo Holdings. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
vespa_add_library(document_util OBJECT
    SOURCES
    bytebuffer.cpp
    compressor.cpp
    lz4compressor.cpp
    zstdcompressor.cpp
    printable.cpp
    serializable.cpp
    stringutil.cpp
    DEPENDS
    AFTER
    document_documentconfig
)
