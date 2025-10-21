package Type.Sink
import Type.{CallType, TypeDefinition}

object SinkGroups {
  private val COMMON_SINKS: Seq[TypeDefinition] = Seq(
    // Code execution functions
    CallType("eval"),
    CallType("Function"),
    CallType("setTimeout"),
    CallType("setInterval"),
    CallType("exec"),
    CallType("execSync"),
    CallType("spawn"),
    CallType("spawnSync"),

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
    CallType("fetch")
  )

  private val NETWORK_SINKS: Seq[TypeDefinition] = Seq(
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
    CallType("fetch")
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
    CallType("crypto.createCipher"),
    CallType("crypto.createDecipher"),
    CallType("crypto.createHash"),
    CallType("crypto.sign"),
    CallType("crypto.verify")
  )

  private val FILE_SYSTEM_SINKS: Seq[TypeDefinition] = Seq(
    // File system write functions
    CallType("fs.writeFile"),
    CallType("fs.writeFileSync"),
    CallType("fs.appendFile"),
    CallType("fs.appendFileSync"),
    CallType("fs.createWriteStream")
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
    CallType("fetch"),
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
    CallType("path.resolve"),
    CallType("path.join"),
    CallType("path.normalize"),
    CallType("fs.readFile"),
    CallType("fs.writeFile")
  )

  private val OS_COMMAND_SINKS: Seq[TypeDefinition] = Seq(
    CallType("child_process.exec"),
    CallType("child_process.execSync"),
    CallType("child_process.spawn"),
    CallType("child_process.execFile"),
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
    CallType("fetch"),
    CallType("request.get"),
    CallType("curl")
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
      SSRF_SINKS).toSet
  }
}
