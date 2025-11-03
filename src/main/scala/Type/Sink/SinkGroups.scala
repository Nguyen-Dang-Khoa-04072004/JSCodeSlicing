package Type.Sink
import Type.{CallType, RegexType, TypeDefinition}

object SinkGroups {
  private val COMMON_SINKS: Seq[TypeDefinition] = Seq(
    // Code execution functions - Critical for malware detection
    CallType("eval"), // Direct eval
    CallType("Function"), // new Function() - dynamic code
    CallType("setTimeout"), // Delayed execution
    CallType("setInterval"), // Repeated execution
    CallType("exec"), // Child process execution
    CallType("execSync"), // Synchronous execution
    CallType("spawn"), // Spawn new process
    CallType("spawnSync"), // Synchronous spawn
    CallType("send"), // IPC message sending

    // File system write functions
    CallType("fs.writeFile"),
    CallType("fs.writeFileSync"),
    CallType("fs.appendFile"),
    CallType("fs.appendFileSync"),
    CallType("fs.createWriteStream"),

    // Network send functions
    CallType("http.request"),
    CallType("https.request"),
    CallType("axios.post"),
    CallType("axios.put"),
    CallType("axios.patch"),
    CallType("axios.delete"),
//    CallType("fetch"),
    CallType("write"), // e.g. req.write(...)
    CallType("end"), // e.g. req.end()
    CallType("destroy"), // e.g. req.destroy()

    // Logging functions (potential data-leak sinks)
    CallType("console.log"),
    CallType("console.error"),
    CallType("console.warn"),
    
    // Windows Scripting Host (WSH) Execution - Common in malware
    CallType("run"), // WScript.Shell.Run() - executes commands
    CallType("Run"), // Case variation
    CallType("Exec"), // WScript.Shell.Exec() - executes with output
    CallType("ShellExecute"), // Shell.Application.ShellExecute()
    
    // PowerShell and CMD execution
    CallType("Invoke-Expression"), // PowerShell code execution
    CallType("Start-Process"), // PowerShell process start
    CallType("Invoke-Command"), // PowerShell remote execution
    CallType("Invoke-Item"), // PowerShell file execution
    
    // Dynamic code loading - Obfuscation techniques
    CallType("DownloadFile"), // System.Net.Webclient.DownloadFile
    CallType("DownloadString"), // Download and execute
    CallType("DownloadData"), // Download binary data
    
    // COM Object methods - Windows automation
    CallType("CreateObject"), // Create ActiveX/COM objects
    CallType("GetObject"), // Get existing objects
    
    // Computed member access - Obfuscated method calls
    RegexType(".*\\[.*\\].*"), // Matches computed property access like obj[prop]()
    
    // Malicious string patterns in code
    RegexType(".*cmd\\.exe.*"), // Command execution
    RegexType(".*powershell.*"), // PowerShell execution
    
    // Other dangerous operations
    CallType("execFile"), // Execute file directly
    CallType("execFileSync"), // Sync file execution
    CallType("fork"), // Fork child process
    CallType("execPath") // Path to executable
  )

  private val NETWORK_SINKS: Seq[TypeDefinition] = Seq(
    // HTTPS operations
    CallType("https.Agent"),
    CallType("https.Server"),
    CallType("https.createServer"),
    CallType("https.get"),
    CallType("https.request"),
    
    // HTTP operations
    CallType("http._connectionListener"),
    CallType("http.Agent"),
    CallType("http.ClientRequest"),
    CallType("http.IncomingMessage"),
    CallType("http.OutgoingMessage"),
    CallType("http.Server"),
    CallType("http.ServerResponse"),
    CallType("http.createServer"),
    CallType("http.validateHeaderName"),
    CallType("http.validateHeaderValue"),
    CallType("http.get"),
    CallType("http.request"),
    CallType("http.setMaxIdleHTTPParsers"),
    
    // Net operations
    CallType("net._createServerHandle"),
    CallType("net._normalizeArgs"),
    CallType("net._setSimultaneousAccepts"),
    CallType("net.BlockList"),
    CallType("net.SocketAddress"),
    CallType("net.connect"),
    CallType("net.createConnection"),
    CallType("net.createServer"),
    CallType("net.isIP"),
    CallType("net.isIPv4"),
    CallType("net.isIPv6"),
    CallType("net.Server"),
    CallType("net.Socket"),
    CallType("net.Stream"),
    CallType("net.getDefaultAutoSelectFamily"),
    CallType("net.setDefaultAutoSelectFamily"),
    CallType("net.getDefaultAutoSelectFamilyAttemptTimeout"),
    CallType("net.setDefaultAutoSelectFamilyAttemptTimeout"),
    
    // TLS operations
    CallType("tls.getCiphers"),
    CallType("tls.convertALPNProtocols"),
    CallType("tls.checkServerIdentity"),
    CallType("tls.createSecureContext"),
    CallType("tls.SecureContext"),
    CallType("tls.TLSSocket"),
    CallType("tls.Server"),
    CallType("tls.createServer"),
    CallType("tls.connect"),
    CallType("tls.createSecurePair"),
    
    // URL operations
    CallType("url.Url"),
    CallType("url.parse"),
    CallType("url.resolve"),
    CallType("url.resolveObject"),
    CallType("url.format"),
    CallType("url.URL"),
    CallType("url.URLSearchParams"),
    CallType("url.domainToASCII"),
    CallType("url.domainToUnicode"),
    CallType("url.pathToFileURL"),
    CallType("url.fileURLToPath"),
    CallType("url.urlToHttpOptions"),
    
    // DNS operations
    CallType("dns.lookup"),
    CallType("dns.lookupService"),
    CallType("dns.Resolver"),
    CallType("dns.getDefaultResultOrder"),
    CallType("dns.setDefaultResultOrder"),
    CallType("dns.setServers"),
    CallType("dns.getServers"),
    CallType("dns.resolve"),
    CallType("dns.resolve4"),
    CallType("dns.resolve6"),
    CallType("dns.resolveAny"),
    CallType("dns.resolveCaa"),
    CallType("dns.resolveCname"),
    CallType("dns.resolveMx"),
    CallType("dns.resolveNaptr"),
    CallType("dns.resolveNs"),
    CallType("dns.resolvePtr"),
    CallType("dns.resolveSoa"),
    CallType("dns.resolveSrv"),
    CallType("dns.resolveTxt"),
    CallType("dns.reverse"),
    
    // Querystring operations
    CallType("querystring.unescapeBuffer"),
    CallType("querystring.unescape"),
    CallType("querystring.escape"),
    CallType("querystring.stringify"),
    CallType("querystring.encode"),
    CallType("querystring.parse"),
    CallType("querystring.decode"),
    
    // Socket send functions
    CallType("net.Socket.write"),
    CallType("dgram.Socket.send"),
    CallType("socket.emit"),
    CallType("ws.send")
  )

  private val DATA_LEAK_SINKS: Seq[TypeDefinition] = Seq(
    // Console output functions
    CallType("console.log"),
    CallType("console.error"),
    CallType("console.warn"),
    CallType("console.info"),

    // File output functions
    CallType("fs.writeFile"),
    CallType("fs.appendFile"),

    // Network send functions
    CallType("http.request"),
    CallType("https.request"),
    CallType("axios.post"),
//    CallType("fetch")
  )

  private val DATABASE_SINKS: Seq[TypeDefinition] = Seq(
    // Database query functions
    CallType("db.query"),
    CallType("db.execute"),
    CallType("sequelize.query"),
    CallType("mongoose.model.save"),
    CallType("mongodb.collection.insert"),
    CallType("mysql.query"),
    CallType("pg.query")
  )

  private val CRYPTO_SINKS: Seq[TypeDefinition] = Seq(
    // Cryptographic operations
    CallType("crypto.checkPrime"),
    CallType("crypto.checkPrimeSync"),
    CallType("crypto.createCipheriv"),
    CallType("crypto.createDecipheriv"),
    CallType("crypto.createDiffieHellman"),
    CallType("crypto.createDiffieHellmanGroup"),
    CallType("crypto.createECDH"),
    CallType("crypto.createHash"),
    CallType("crypto.createHmac"),
    CallType("crypto.createPrivateKey"),
    CallType("crypto.createPublicKey"),
    CallType("crypto.createSecretKey"),
    CallType("crypto.createSign"),
    CallType("crypto.createVerify"),
    CallType("crypto.diffieHellman"),
    CallType("crypto.generatePrime"),
    CallType("crypto.generatePrimeSync"),
    CallType("crypto.getCiphers"),
    CallType("crypto.getCipherInfo"),
    CallType("crypto.getCurves"),
    CallType("crypto.getDiffieHellman"),
    CallType("crypto.getHashes"),
    CallType("crypto.hkdf"),
    CallType("crypto.hkdfSync"),
    CallType("crypto.pbkdf2"),
    CallType("crypto.pbkdf2Sync"),
    CallType("crypto.generateKeyPair"),
    CallType("crypto.generateKeyPairSync"),
    CallType("crypto.generateKey"),
    CallType("crypto.generateKeySync"),
    CallType("crypto.privateDecrypt"),
    CallType("crypto.privateEncrypt"),
    CallType("crypto.publicDecrypt"),
    CallType("crypto.publicEncrypt"),
    CallType("crypto.randomBytes"),
    CallType("crypto.randomFill"),
    CallType("crypto.randomFillSync"),
    CallType("crypto.randomInt"),
    CallType("crypto.randomUUID"),
    CallType("crypto.scrypt"),
    CallType("crypto.scryptSync"),
    CallType("crypto.sign"),
    CallType("crypto.setEngine"),
    CallType("crypto.timingSafeEqual"),
    CallType("crypto.getFips"),
    CallType("crypto.setFips"),
    CallType("crypto.verify"),
    CallType("crypto.Certificate"),
    CallType("crypto.Cipher"),
    CallType("crypto.Cipheriv"),
    CallType("crypto.Decipher"),
    CallType("crypto.Decipheriv"),
    CallType("crypto.DiffieHellman"),
    CallType("crypto.DiffieHellmanGroup"),
    CallType("crypto.ECDH"),
    CallType("crypto.Hash"),
    CallType("crypto.Hmac"),
    CallType("crypto.KeyObject"),
    CallType("crypto.Sign"),
    CallType("crypto.Verify"),
    CallType("crypto.X509Certificate"),
    CallType("crypto.secureHeapUsed"),
    CallType("crypto.createCipher"),
    CallType("crypto.createDecipher"),
    CallType("crypto.getRandomValues"),
    CallType("crypto.prng"),
    CallType("crypto.pseudoRandomBytes"),
    CallType("crypto.rng")
  )

  private val FILE_SYSTEM_SINKS: Seq[TypeDefinition] = Seq(
    // File system operations
    CallType("fs.appendFile"),
    CallType("fs.appendFileSync"),
    CallType("fs.access"),
    CallType("fs.accessSync"),
    CallType("fs.chown"),
    CallType("fs.chownSync"),
    CallType("fs.chmod"),
    CallType("fs.chmodSync"),
    CallType("fs.close"),
    CallType("fs.closeSync"),
    CallType("fs.copyFile"),
    CallType("fs.copyFileSync"),
    CallType("fs.cp"),
    CallType("fs.cpSync"),
    CallType("fs.createReadStream"),
    CallType("fs.createWriteStream"),
    CallType("fs.exists"),
    CallType("fs.existsSync"),
    CallType("fs.fchown"),
    CallType("fs.fchownSync"),
    CallType("fs.fchmod"),
    CallType("fs.fchmodSync"),
    CallType("fs.fdatasync"),
    CallType("fs.fdatasyncSync"),
    CallType("fs.fstat"),
    CallType("fs.fstatSync"),
    CallType("fs.fsync"),
    CallType("fs.fsyncSync"),
    CallType("fs.ftruncate"),
    CallType("fs.ftruncateSync"),
    CallType("fs.futimes"),
    CallType("fs.futimesSync"),
    CallType("fs.lchown"),
    CallType("fs.lchownSync"),
    CallType("fs.link"),
    CallType("fs.linkSync"),
    CallType("fs.lstat"),
    CallType("fs.lstatSync"),
    CallType("fs.lutimes"),
    CallType("fs.lutimesSync"),
    CallType("fs.mkdir"),
    CallType("fs.mkdirSync"),
    CallType("fs.mkdtemp"),
    CallType("fs.mkdtempSync"),
    CallType("fs.open"),
    CallType("fs.openSync"),
    CallType("fs.openAsBlob"),
    CallType("fs.readdir"),
    CallType("fs.readdirSync"),
    CallType("fs.read"),
    CallType("fs.readSync"),
    CallType("fs.readv"),
    CallType("fs.readvSync"),
    CallType("fs.readFile"),
    CallType("fs.readFileSync"),
    CallType("fs.readlink"),
    CallType("fs.readlinkSync"),
    CallType("fs.realpath"),
    CallType("fs.realpathSync"),
    CallType("fs.rename"),
    CallType("fs.renameSync"),
    CallType("fs.rm"),
    CallType("fs.rmSync"),
    CallType("fs.rmdir"),
    CallType("fs.rmdirSync"),
    CallType("fs.stat"),
    CallType("fs.statfs"),
    CallType("fs.statSync"),
    CallType("fs.statfsSync"),
    CallType("fs.symlink"),
    CallType("fs.symlinkSync"),
    CallType("fs.truncate"),
    CallType("fs.truncateSync"),
    CallType("fs.unwatchFile"),
    CallType("fs.unlink"),
    CallType("fs.unlinkSync"),
    CallType("fs.utimes"),
    CallType("fs.utimesSync"),
    CallType("fs.watch"),
    CallType("fs.watchFile"),
    CallType("fs.writeFile"),
    CallType("fs.writeFileSync"),
    CallType("fs.write"),
    CallType("fs.writeSync"),
    CallType("fs.writev"),
    CallType("fs.writevSync"),
    CallType("fs.Dirent"),
    CallType("fs.Stats"),
    CallType("fs.ReadStream"),
    CallType("fs.WriteStream"),
    CallType("fs.FileReadStream"),
    CallType("fs.FileWriteStream"),
    CallType("fs._toUnixTimestamp"),
    CallType("fs.Dir"),
    CallType("fs.opendir"),
    CallType("fs.opendirSync")
  )

  private val INPUT_VALIDATION_SINKS: Seq[TypeDefinition] = Seq(
    // Sanitization functions (NOT SINKS - should be removed or moved)
    CallType("validator.escape"),
    CallType("validator.sanitize"),
    CallType("xss"),
    CallType("express-validator")
  )

  private val DATA_EXFILTRATION_SINKS: Seq[TypeDefinition] = Seq(
    // Data exfiltration functions
    CallType("https.request"),
    CallType("http.request"),
//    CallType("fetch"),
    CallType("axios.post"),
    CallType("fetch.post")
  )

  private val SQL_INJECTION_SINKS: Seq[TypeDefinition] = Seq(
    // SQL query functions
    CallType("query"),
    CallType("execute"),
    CallType("db.query"),
    CallType("db.execute"),
    CallType("connection.query"),
    CallType("pool.query")
  )

  private val NOSQL_INJECTION_SINKS: Seq[TypeDefinition] = Seq(
    // NoSQL query functions
    CallType("find"),
    CallType("findOne"),
    CallType("updateOne"),
    CallType("deleteOne"),
    CallType("insertOne"),
    CallType("mongodb.find")
  )

  private val TEMPLATE_INJECTION_SINKS: Seq[TypeDefinition] = Seq(
    // Template rendering functions
    CallType("render"),
    CallType("compile"),
    CallType("template"),
    CallType("handlebars.compile"),
    CallType("ejs.render"),
    CallType("pug.render")
  )

  private val XSS_SINKS: Seq[TypeDefinition] = Seq(
    // DOM manipulation functions
    CallType("innerHTML"),
    CallType("appendChild"),
    CallType("insertBefore"),
    CallType("document.write")
  )

  private val REDIRECT_SINKS: Seq[TypeDefinition] = Seq(
    // URL redirect functions
    CallType("window.location"),
    CallType("window.location.href"),
    CallType("location.replace"),
    CallType("res.redirect"),
    CallType("res.location")
  )

  private val PATH_TRAVERSAL_SINKS: Seq[TypeDefinition] = Seq(
    // Path operations
    CallType("path.resolve"),
    CallType("path.normalize"),
    CallType("path.isAbsolute"),
    CallType("path.join"),
    CallType("path.relative"),
    CallType("path.toNamespacedPath"),
    CallType("path.dirname"),
    CallType("path.basename"),
    CallType("path.extname"),
    CallType("path.format"),
    CallType("path.parse"),
    CallType("path._makeLong"),
    CallType("fs.readFile"),
    CallType("fs.writeFile")
  )

  private val OS_COMMAND_SINKS: Seq[TypeDefinition] = Seq(
    // Child process operations
    CallType("child_process._forkChild"),
    CallType("child_process.ChildProcess"),
    CallType("child_process.exec"),
    CallType("child_process.execFile"),
    CallType("child_process.execFileSync"),
    CallType("child_process.execSync"),
    CallType("child_process.fork"),
    CallType("child_process.spawn"),
    CallType("child_process.spawnSync"),
    
    // Process operations
    CallType("process._rawDebug"),
    CallType("process.binding"),
    CallType("process._linkedBinding"),
    CallType("process.dlopen"),
    CallType("process.uptime"),
    CallType("process._getActiveRequests"),
    CallType("process._getActiveHandles"),
    CallType("process.getActiveResourcesInfo"),
    CallType("process.reallyExit"),
    CallType("process._kill"),
    CallType("process.cpuUsage"),
    CallType("process.resourceUsage"),
    CallType("process.memoryUsage"),
    CallType("process.constrainedMemory"),
    CallType("process.kill"),
    CallType("process.exit"),
    CallType("process.hrtime"),
    CallType("process.openStdin"),
    CallType("process.getuid"),
    CallType("process.geteuid"),
    CallType("process.getgid"),
    CallType("process.getegid"),
    CallType("process.getgroups"),
    CallType("process.assert"),
    CallType("process._fatalException"),
    CallType("process.setUncaughtExceptionCaptureCallback"),
    CallType("process.hasUncaughtExceptionCaptureCallback"),
    CallType("process.emitWarning"),
    CallType("process.nextTick"),
    CallType("process._tickCallback"),
    CallType("process.setSourceMapsEnabled"),
    CallType("process._debugProcess"),
    CallType("process._debugEnd"),
    CallType("process._startProfilerIdleNotifier"),
    CallType("process._stopProfilerIdleNotifier"),
    CallType("process.abort"),
    CallType("process.umask"),
    CallType("process.chdir"),
    CallType("process.cwd"),
    CallType("process.initgroups"),
    CallType("process.setgroups"),
    CallType("process.setegid"),
    CallType("process.seteuid"),
    CallType("process.setgid"),
    CallType("process.setuid"),
    
    // OS operations
    CallType("os.arch"),
    CallType("os.availableParallelism"),
    CallType("os.cpus"),
    CallType("os.endianness"),
    CallType("os.freemem"),
    CallType("os.getPriority"),
    CallType("os.homedir"),
    CallType("os.hostname"),
    CallType("os.loadavg"),
    CallType("os.networkInterfaces"),
    CallType("os.platform"),
    CallType("os.release"),
    CallType("os.setPriority"),
    CallType("os.tmpdir"),
    CallType("os.totalmem"),
    CallType("os.type"),
    CallType("os.userInfo"),
    CallType("os.uptime"),
    CallType("os.version"),
    CallType("os.machine"),
    
    CallType("os.system")
  )

  private val INSECURE_DESERIALIZATION_SINKS: Seq[TypeDefinition] = Seq(
    CallType("JSON.parse"),
    CallType("pickle.loads"),
    CallType("unserialize"),
    CallType("eval"),
    CallType("Function")
  )

  private val WEAK_CRYPTO_SINKS: Seq[TypeDefinition] = Seq(
    CallType("crypto.createCipher"), // ❌ Deprecated
    CallType("crypto.createDecipher"), // ❌ Deprecated
    CallType("md5"), // ❌ Weak
    CallType("sha1"), // ❌ Weak
    CallType("Math.random") // ❌ Insecure for crypto
  )

  private val XXE_SINKS: Seq[TypeDefinition] = Seq(
    CallType("xml.parse"),
    CallType("xml2js.parseString"),
    CallType("libxmljs.parseXml"),
    CallType("DOMParser")
  )

  private val SSRF_SINKS: Seq[TypeDefinition] = Seq(
    CallType("http.request"),
    CallType("https.request"),
    CallType("axios.get"),
//    CallType("fetch"),
    CallType("request.get"),
    CallType("curl")
  )

  private val STREAM_SINKS: Seq[TypeDefinition] = Seq(
    // Stream operations
    CallType("stream.isDestroyed"),
    CallType("stream.isDisturbed"),
    CallType("stream.isErrored"),
    CallType("stream.isReadable"),
    CallType("stream.isWritable"),
    CallType("stream.Readable"),
    CallType("stream.Writable"),
    CallType("stream.Duplex"),
    CallType("stream.Transform"),
    CallType("stream.PassThrough"),
    CallType("stream.pipeline"),
    CallType("stream.addAbortSignal"),
    CallType("stream.finished"),
    CallType("stream.destroy"),
    CallType("stream.compose"),
    CallType("stream.setDefaultHighWaterMark"),
    CallType("stream.getDefaultHighWaterMark"),
    CallType("stream.Stream"),
    CallType("stream._isUint8Array"),
    CallType("stream._uint8ArrayToBuffer")
  )

  private val COMPRESSION_SINKS: Seq[TypeDefinition] = Seq(
    // Zlib operations
    CallType("zlib.Deflate"),
    CallType("zlib.Inflate"),
    CallType("zlib.Gzip"),
    CallType("zlib.Gunzip"),
    CallType("zlib.DeflateRaw"),
    CallType("zlib.InflateRaw"),
    CallType("zlib.Unzip"),
    CallType("zlib.BrotliCompress"),
    CallType("zlib.BrotliDecompress"),
    CallType("zlib.deflate"),
    CallType("zlib.deflateSync"),
    CallType("zlib.gzip"),
    CallType("zlib.gzipSync"),
    CallType("zlib.deflateRaw"),
    CallType("zlib.deflateRawSync"),
    CallType("zlib.unzip"),
    CallType("zlib.unzipSync"),
    CallType("zlib.inflate"),
    CallType("zlib.inflateSync"),
    CallType("zlib.gunzip"),
    CallType("zlib.gunzipSync"),
    CallType("zlib.inflateRaw"),
    CallType("zlib.inflateRawSync"),
    CallType("zlib.brotliCompress"),
    CallType("zlib.brotliCompressSync"),
    CallType("zlib.brotliDecompress"),
    CallType("zlib.brotliDecompressSync"),
    CallType("zlib.createDeflate"),
    CallType("zlib.createInflate"),
    CallType("zlib.createDeflateRaw"),
    CallType("zlib.createInflateRaw"),
    CallType("zlib.createGzip"),
    CallType("zlib.createGunzip"),
    CallType("zlib.createUnzip"),
    CallType("zlib.createBrotliCompress"),
    CallType("zlib.createBrotliDecompress")
  )

  private val BUFFER_SINKS: Seq[TypeDefinition] = Seq(
    // Buffer operations
    CallType("buffer.Buffer"),
    CallType("buffer.SlowBuffer"),
    CallType("buffer.transcode"),
    CallType("buffer.isUtf8"),
    CallType("buffer.isAscii"),
    CallType("buffer.btoa"),
    CallType("buffer.atob"),
    CallType("buffer.Blob"),
    CallType("buffer.resolveObjectURL"),
    CallType("buffer.File")
  )

  def getAllSinks: Set[TypeDefinition] = {
    (COMMON_SINKS ++
      NETWORK_SINKS ++
      DATA_LEAK_SINKS ++
      DATABASE_SINKS ++
      CRYPTO_SINKS ++
      FILE_SYSTEM_SINKS ++
      INPUT_VALIDATION_SINKS ++
      DATA_EXFILTRATION_SINKS ++
      SQL_INJECTION_SINKS ++
      NOSQL_INJECTION_SINKS ++
      TEMPLATE_INJECTION_SINKS ++
      XSS_SINKS ++
      REDIRECT_SINKS ++
      PATH_TRAVERSAL_SINKS ++
      OS_COMMAND_SINKS ++
      INSECURE_DESERIALIZATION_SINKS ++
      WEAK_CRYPTO_SINKS ++
      XXE_SINKS ++
      SSRF_SINKS ++
      STREAM_SINKS ++
      COMPRESSION_SINKS ++
      BUFFER_SINKS).toSet
  }
}
