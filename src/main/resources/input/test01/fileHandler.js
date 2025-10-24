// ============================================================================
// fileHandler.js - Thao tác với file hệ thống
// ============================================================================

const fs = require("fs");
const path = require("path");
const config = require("./config");

class FileHandler {
  constructor() {
    this.encoding = "utf-8";
    this.dataDir = config.dataDir;
    this.ensureDataDir();
  }

  ensureDataDir() {
    try {
      if (!fs.existsSync(this.dataDir)) {
        fs.mkdirSync(this.dataDir, { recursive: true });
        console.log(`[FileHandler] Created data directory: ${this.dataDir}`);
      }
    } catch (error) {
      console.error(
        `[FileHandler] Error creating data directory: ${error.message}`
      );
    }
  }

  readFile(filename) {
    try {
      const filepath = path.join(this.dataDir, filename);
      const content = fs.readFileSync(filepath, this.encoding);
      console.log(
        `[FileHandler] Read file: ${filename} (${content.length} bytes)`
      );
      return content;
    } catch (error) {
      console.error(
        `[FileHandler] Error reading file ${filename}: ${error.message}`
      );
      return null;
    }
  }

  writeFile(filename, content) {
    try {
      const filepath = path.join(this.dataDir, filename);
      const dirpath = path.dirname(filepath);

      if (!fs.existsSync(dirpath)) {
        fs.mkdirSync(dirpath, { recursive: true });
      }

      fs.writeFileSync(filepath, content, this.encoding);
      console.log(
        `[FileHandler] Wrote file: ${filename} (${content.length} bytes)`
      );
      return true;
    } catch (error) {
      console.error(
        `[FileHandler] Error writing file ${filename}: ${error.message}`
      );
      return false;
    }
  }

  appendFile(filename, content) {
    try {
      const filepath = path.join(this.dataDir, filename);
      fs.appendFileSync(filepath, content, this.encoding);
      console.log(`[FileHandler] Appended to file: ${filename}`);
      return true;
    } catch (error) {
      console.error(
        `[FileHandler] Error appending to file ${filename}: ${error.message}`
      );
      return false;
    }
  }

  deleteFile(filename) {
    try {
      const filepath = path.join(this.dataDir, filename);
      if (fs.existsSync(filepath)) {
        fs.unlinkSync(filepath);
        console.log(`[FileHandler] Deleted file: ${filename}`);
        return true;
      }
      return false;
    } catch (error) {
      console.error(
        `[FileHandler] Error deleting file ${filename}: ${error.message}`
      );
      return false;
    }
  }

  listFiles(subdir = "") {
    try {
      const dirpath = subdir ? path.join(this.dataDir, subdir) : this.dataDir;
      const files = fs.readdirSync(dirpath);
      console.log(
        `[FileHandler] Listed files in ${subdir || "root"}: ${
          files.length
        } items`
      );
      return files;
    } catch (error) {
      console.error(`[FileHandler] Error listing files: ${error.message}`);
      return [];
    }
  }

  fileExists(filename) {
    const filepath = path.join(this.dataDir, filename);
    return fs.existsSync(filepath);
  }

  getFileStats(filename) {
    try {
      const filepath = path.join(this.dataDir, filename);
      const stats = fs.statSync(filepath);
      return {
        size: stats.size,
        created: stats.birthtime,
        modified: stats.mtime,
        isFile: stats.isFile(),
        isDirectory: stats.isDirectory(),
      };
    } catch (error) {
      console.error(`[FileHandler] Error getting file stats: ${error.message}`);
      return null;
    }
  }

  backupFile(filename) {
    try {
      const timestamp = new Date().toISOString().replace(/[:.]/g, "-");
      const backupName = `backup-${timestamp}-${filename}`;
      const sourcePath = path.join(this.dataDir, filename);
      const backupPath = path.join(this.dataDir, "backups", backupName);

      const backupDir = path.dirname(backupPath);
      if (!fs.existsSync(backupDir)) {
        fs.mkdirSync(backupDir, { recursive: true });
      }

      fs.copyFileSync(sourcePath, backupPath);
      console.log(`[FileHandler] Backed up file: ${filename} -> ${backupName}`);
      return backupName;
    } catch (error) {
      console.error(`[FileHandler] Error backing up file: ${error.message}`);
      return null;
    }
  }
}

module.exports = new FileHandler();
