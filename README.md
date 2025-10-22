# JS Code Slicing

A Scala-based project for analyzing and slicing JavaScript code using AST generation. This project leverages [Joern](https://joern.io/) and [astgen](https://github.com/joernio/astgen) for abstract syntax tree (AST) generation and analysis.

## Prerequisites

- **Scala 3.6.4** or later
- **SBT** (Scala Build Tool)
- **astgen** binary (Linux executable for AST generation)

## Setup Instructions

### 1. Configure astgen Binary

Before running the project, you need to set up the astgen binary:

```bash
# Make the astgen binary executable
chmod +x ./astgen/astgen-linux

# Export the ASTGEN_BIN environment variable (on Linux/macOS)
export ASTGEN_BIN=/mnt/d/HK251/Malware/datasets/code-slice-2/JSCodeSlicing/astgen/astgen-linux
```

**Note for Windows users:** If you're on Windows with WSL, use the command above. For native Windows, you may need to adjust the path accordingly.

### 2. Project Dependencies

The project uses the following Joern libraries (Scala 3.6.4):

- `io.joern:joern-cli:4.0.436` - Joern CLI tools
- `io.joern:jssrc2cpg:4.0.436` - JavaScript source to Code Property Graph converter
- `io.joern:x2cpg:4.0.436` - Generic CPG conversion utilities
- `com.google.guava:guava:33.0.0-jre` - Google utilities library
- `org.slf4j:slf4j-simple:2.0.16` - Logging framework

Dependencies are automatically resolved from the [Joern repository](https://repo.joern.io).

## How to Run

### Basic Usage

1. **Prepare input files:**

   - Place your JavaScript/code packages in `src/main/resources/input/` directory

2. **Run the project:**

   ```bash
   sbt run
   ```

3. **Collect output:**
   - The processed code slices will be generated and saved to `src/main/resources/output/`
   - Each package from the input will have a corresponding output directory

### Directory Structure

```
src/main/resources/
├── input/           # Place your code packages here
│   └── [package-name]/
└── output/          # Generated output files appear here
    └── [package-name]/
```

## Project Architecture

The project consists of the following main components:

- **Main.scala** - Entry point that orchestrates the code slicing process
- **CodeSlice/** - Core slicing logic and algorithms
- **FileProcessor/** - File I/O operations and package management
- **Type/** - Type definitions and data structures

## Workflow

1. The application scans `src/main/resources/input/` for package directories
2. For each package, it creates a corresponding output directory in `src/main/resources/output/`
3. Each package is processed using the `CodeSliceImp` class
4. Results are stored in the respective output directory

## Building

To build the project without running:

```bash
sbt compile
```

To create a packaged distribution:

```bash
sbt assembly
```

## Related Resources

- [Joern Documentation](https://docs.joern.io/)
- [astgen GitHub Repository](https://github.com/joernio/astgen)
- [Code Property Graph (CPG)](https://cpg.joern.io/)

## License

See LICENSE file for details.

## Support

For issues and questions, please refer to the project repository or the Joern community documentation.
