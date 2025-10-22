package FileProcessor
import java.io.{File, FileWriter, BufferedWriter}
import scala.io.Source

object IOFileProcessorImpl extends IOFileProcessor {

  override def listPackageDirectories(directoryPath: String): Seq[String] = {
    val directory = new File(directoryPath)

    if (!directory.exists() || !directory.isDirectory) {
      println(
        s"Warning: Directory does not exist or is not a directory: $directoryPath"
      )
      return Seq.empty
    }

    directory
      .listFiles()
      .filter(_.isDirectory)
      .map(_.getAbsolutePath)
      .sorted
  }

  override def loadCheckpoint(checkpointFilePath: String): Set[String] = {
    val checkpointFile = new File(checkpointFilePath)

    if (!checkpointFile.exists()) {
      checkpointFile.createNewFile()
    }

    try {
      val source = Source.fromFile(checkpointFile)
      val processedPackages = source
        .getLines()
        .map(_.trim)
        .filter(_.nonEmpty)
        .toSet

      source.close()
      processedPackages
    } catch {
      case e: Exception =>
        println(s"Error reading checkpoint file: ${e.getMessage}")
        Set.empty
    }
  }

  override def saveCheckpoint(
      checkpointFilePath: String,
      packageName: String
  ): Boolean = {
    try {
      val checkpointFile = new File(checkpointFilePath)
      checkpointFile.getParentFile.mkdirs()

      val fileWriter = new FileWriter(checkpointFile, true)
      val bufferedWriter = new BufferedWriter(fileWriter)
      bufferedWriter.write(packageName + "\n")
      bufferedWriter.close()
      fileWriter.close()
      true
    } catch {
      case e: Exception =>
        println(s"Error saving checkpoint: ${e.getMessage}")
        false
    }
  }

  override def isPackageProcessed(
      packageName: String,
      processedPackages: Set[String]
  ): Boolean = {
    processedPackages.contains(packageName)
  }

  override def saveOutputPackage(
      outputFilePath: String,
      content: String
  ): Boolean = {
    try {
      val outputFile = new File(outputFilePath)
      outputFile.getParentFile.mkdirs()
      val fileWriter = new FileWriter(outputFile)
      val bufferedWriter = new BufferedWriter(fileWriter)
      bufferedWriter.write(content)
      bufferedWriter.close()
      fileWriter.close()
      true
    } catch {
      case e: Exception =>
        println(s"Error saving output package: ${e.getMessage}")
        false
    }
  }

  override def getPackageName(packageDir: String): String = {
    new File(packageDir).getName
  }

  override def createOutputDirectoryIfNotExists(outputDir: String): String = {
    try {
      val dir = new File(outputDir)
      if (!dir.exists()) {
        dir.mkdirs()
      }
      dir.getAbsolutePath()
    } catch {
      case e: Exception =>
        println(s"Error creating output directory: ${e.getMessage}")
        ""
    }
  }
}
