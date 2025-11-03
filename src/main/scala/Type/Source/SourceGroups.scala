package Type.Source

import Type.{CodeType, CallType, RegexType, TypeDefinition}
import jnr.ffi.annotations

object SourceGroups {
  private val COMMON_SOURCES: Seq[TypeDefinition] = Seq(
    // Environment variables
    CodeType("process.argv"), // Biến, không phải function
    CodeType("process.env"), // Biến object

    // Module imports
    CallType("require"), // Function call
    CallType("require.resolve"), // Function call
    CallType("import"), // Function/keyword

    // Config loaders
    CallType("cleanEnv"), // Function call từ envalid
    CallType("dotenv.config"), // Function call từ dotenv

    // Argument parsers
    CallType("yargs.argv"), // Property access từ yargs
    CallType("commander.opts"), // Function call từ commander
    CallType("config.get"), // Function call từ config
    
    // Windows Scripting Host (WSH) - Common malware sources
    CodeType("WScript"), // Global WScript object
    CodeType("ActiveXObject"), // ActiveX object for IE/WSH
    CallType("CreateObject"), // Create COM objects
    CallType("GetObject"), // Get existing COM objects
    
    // Dynamic function creation - Often used in obfuscation
    CallType("Function"), // new Function() constructor
    CodeType("constructor"), // Access to constructor property
    
    // String manipulation - Used in deobfuscation
    CallType("String.fromCharCode"), // Convert char codes to string
    CallType("unescape"), // URL decode
    CallType("decodeURI"), // Decode URI
    CallType("decodeURIComponent"), // Decode URI component
    CallType("atob"), // Base64 decode
    
    // Obfuscation detection - Variable property access
    CodeType("window"), // Global window object
    CodeType("global"), // Global object in Node.js
    CodeType("globalThis") // Universal global object
  )

  private val FILE_SYSTEM_SOURCES: Seq[TypeDefinition] = Seq(
    // File reading functions
    CallType("fs.readFile"), // Function call
    CallType("fs.readFileSync"), // Function call
    CallType("fs.createReadStream"), // Function call
    CallType("fs.readdir"), // Function call
    CallType("fs.readdirSync"), // Function call

    // File stats & metadata
    CallType("fs.stat"), // Get file stats
    CallType("fs.statSync"), // Sync file stats
    CallType("fs.lstat"), // Get link stats
    CallType("fs.access"), // Check file access
    CallType("fs.accessSync"), // Sync access check

    // File existence checks
    CallType("fs.existsSync"), // Check if file exists
    CallType("fs.constants"), // File constants

    // Directory operations
    CallType("fs.opendir"), // Open directory
    CallType("fs.opendirSync") // Sync open directory
  )

  private val NETWORK_SOURCES: Seq[TypeDefinition] = Seq(
    // HTTP/HTTPS request functions (nhận response)
    RegexType(
      """\d+(\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})\d+"""
    ),
    RegexType(
      """.*?https?://\S+.*?"""
    ),
    RegexType(
      """.*?http?://[^\s'"]+.*?"""
    ),
    RegexType(
      """.*?^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$.*?"""
    ),
    CallType("http.get"),
    CallType("http.request"),
    CallType("https.get"),
    CallType("https.request"),
    CallType("axios.get"),
    CallType("axios.request"),
    CallType("axios.post"),
    CallType("fetch"),

    // TCP/UDP socket functions
    CallType("net.connect"),
    CallType("net.Socket"),
    CallType("net.Socket.on"),
    CallType("dgram.createSocket"),
    CallType("socket.on"),

    // DNS resolution
    RegexType(
      """dns\.resolve\(['"`]([a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+?)['"`]"""
    ),

    // Stream/stdin input
    CallType("process.stdin"),
    CallType("process.stdin.on"),

    // Child process execution
    CallType("child_process.exec"),
    CallType("child_process.spawn"),
    CallType("child_process.execFile")
  )

  private val CRYPTO_SOURCES: Seq[TypeDefinition] = Seq(
    // Cryptographic key generation
    CallType("crypto.randomBytes"), // Generate random bytes
    CallType("crypto.generateKeyPair") // Key pair gen
  )

  private val USER_INPUT_SOURCES: Seq[TypeDefinition] = Seq(
    // HTTP request data (Express/Node.js)
    CodeType("req.body"), // Express body parser
    CodeType("req.query"), // Query parameters
    CodeType("req.params"), // Route parameters
    CodeType("req.headers"), // HTTP headers
    CodeType("req.cookies"), // Cookies
    CodeType("req.files"), // Uploaded files
    CodeType("req.url"), // Full request URL
    CodeType("req.method"), // HTTP method
    CodeType("req.path"), // Request path
    CodeType("req.rawBody"), // Raw body

    // Query string & form data parsing
    CallType("URLSearchParams"), // URL parameters
    CallType("querystring.parse"), // Parse query string
    CallType("url.parse"), // Parse URL

    // Form & multipart data
    CallType("formidable.parse"), // Form parsing
    CallType("busboy.on"), // Streaming form parser
    CallType("multer"), // File upload middleware

    // JSON & request body parsing
    CallType("JSON.parse"), // Parse JSON
    CallType("body-parser.json"), // JSON middleware
    CallType("body-parser.urlencoded"), // URL encoded

    // Custom request extractors
    CallType("req.get"), // Get header value
    CallType("req.param"), // Get param (deprecated)
    CallType("req.input"), // Laravel-like input

    // WebSocket & real-time data
    CallType("socket.on"), // WebSocket listener
    CallType("socket.emit"), // WebSocket emit
    CallType("ws.on"), // WebSocket message

    // GraphQL & API queries
    CallType("graphql.parse"), // GraphQL query parse
    CallType("query.variables"), // GraphQL variables

    // Request streams
    CallType("req.pipe"), // Pipe request data
    CallType("req.on"), // Listen to request events
    CodeType("req.data"), // Request data event

    // Browser/Client data
    CodeType("window.location"), // Client URL
    CodeType("document.location"), // Document URL
    CodeType("location.search"), // Query string
    CodeType("localStorage"), // Local storage
    CodeType("sessionStorage"), // Session storage
    CodeType("document.cookie"), // Client cookies

    // Form elements
    CodeType("document.getElementById().value"),
    CodeType("document.querySelector().value"),
    CodeType("form.elements")
  )

  private val EXTERNAL_API_SOURCES: Seq[TypeDefinition] = Seq(
    // API response data
    CallType("axios.get.data"), // Axios response
    CallType("fetch().json()"), // Fetch API
    CallType("http.get.on"), // HTTP callback
    CallType("request.get"), // Request library
    CallType("got.get") // Got library
  )

  private val PACKAGE_JSON_SOURCES: Seq[TypeDefinition] = Seq(
    // Env Cmd Injection
    CodeType("env-cmd"),
    CodeType("cross-env"),

    // Environment & Config packages
    CodeType("dotenv"), // Load .env files
    CodeType("dotenv-safe"), // Safe .env loading
    CodeType("dotenv-expand"), // Expand .env variables
    CodeType("envfile"), // Parse env files

    // Config Management
    CodeType("config"), // Config library
    CodeType("node-config"), // Node config
    CodeType("rc"), // Run commands config
    CodeType("nconf"), // Config management

    // Argument Parsing
    CodeType("yargs"), // CLI argument parser
    CodeType("commander"), // CLI framework
    CodeType("minimist"), // Parse CLI args
    CodeType("getopts"), // Get options

    // Validation & Sanitization
    CodeType("envalid"), // Validate env vars
    CodeType("joi"), // Schema validation
    CodeType("ajv"), // JSON Schema validator

    // File & Directory Access
    CodeType("glob"), // File globbing
    CodeType("fast-glob"), // Fast file globbing
    CodeType("rimraf") // Remove files recursively
  )

  def getAllSources: Set[TypeDefinition] = {
    (COMMON_SOURCES ++
      FILE_SYSTEM_SOURCES ++
      NETWORK_SOURCES ++
      CRYPTO_SOURCES ++
      USER_INPUT_SOURCES ++
      EXTERNAL_API_SOURCES ++
      PACKAGE_JSON_SOURCES).toSet
  }
}
