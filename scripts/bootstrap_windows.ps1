param(
  [switch]$UseChocolatey
)

$ErrorActionPreference = 'Stop'

function Install-WithWinget {
  winget install -e --id EclipseAdoptium.Temurin.21.JDK
  winget install -e --id Python.Python.3.12
  winget install -e --id Gradle.Gradle
}

function Install-WithChoco {
  choco install temurin21 -y
  choco install python --version=3.12.9 -y
  choco install gradle -y
}

if ($UseChocolatey) {
  Install-WithChoco
} else {
  Install-WithWinget
}

java -version
python --version
gradle --version
