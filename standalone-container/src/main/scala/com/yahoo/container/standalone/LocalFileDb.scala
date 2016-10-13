// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.container.standalone

import java.io.File
import java.lang.reflect.Constructor
import java.util
import java.util.concurrent.TimeUnit
import com.yahoo.config.FileReference
import com.yahoo.config.application.api.FileRegistry
import com.yahoo.config.application.api.FileRegistry.Entry
import com.yahoo.filedistribution.fileacquirer.FileAcquirer
import com.yahoo.net.HostName
import scala.collection.JavaConversions._


import LocalFileDb._
import scala.collection.mutable
import java.nio.file.Path


/**
 * FileAcquirer and FileRegistry working on a local directory.
 * @author tonytv
 */
class LocalFileDb(appPath: Path) extends FileAcquirer with FileRegistry {
  private val fileReferenceToFile = mutable.Map[FileReference, File]()

  /** *** FileAcquirer overrides *****/
  def waitFor(reference: FileReference, l: Long, timeUnit: TimeUnit): File = {
    synchronized {
      fileReferenceToFile.get(reference).getOrElse {
        throw new RuntimeException("Invalid file reference " + reference)
      }
    }
  }

  override def shutdown() {}

  /** *** FileRegistry overrides *****/
  def addFile(relativePath: String): FileReference = {
    val file = appPath.resolve(relativePath).toFile
    if (!file.exists) {
      throw new RuntimeException("The file does not exist: " + file.getPath)
    }

    val fileReference: FileReference = fileReferenceConstructor.newInstance("LocalFileDb:" + relativePath)
    fileReferenceToFile.put(fileReference, file)
    fileReference
  }

  def fileSourceHost: String =
    HostName.getHostName

  def allRelativePaths: java.util.Set[String] = {
    new java.util.HashSet(fileReferenceToFile.values.map(_.getPath))
  }

  override def export(): util.List[Entry] = {
    new java.util.ArrayList(fileReferenceToFile.keys.map{ (ref: FileReference) => new Entry(fileReferenceToFile.get(ref).get.getPath, ref)})
  }
}

object LocalFileDb {
  private def createFileReferenceConstructor: Constructor[FileReference] = {
    val method: Constructor[FileReference] = classOf[FileReference].getDeclaredConstructor(classOf[String])
    method.setAccessible(true)
    method
  }

  private val fileReferenceConstructor: Constructor[FileReference] = createFileReferenceConstructor
}
