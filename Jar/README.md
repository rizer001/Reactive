# Jar — prebuilt Reactive server binaries

This directory contains prebuilt `Reactive-paperclip.jar` files produced by the
`copyServerJar` Gradle task. They can be downloaded straight from GitHub
(click the file → Download) so you don't need to build from source.

## Download

1. Open [Jar/](.) on GitHub.
2. Click `Reactive-paperclip.jar` → **Download**.
3. Verify the SHA-256 checksum (see below).

## Verify

Each jar is paired with a `Reactive-paperclip.jar.sha256` checksum file. To
verify the download on Windows (PowerShell):

```powershell
# Get the expected hash from GitHub (Trim() strips possible CRLF)
$expected = ((Get-Content Jar\Reactive-paperclip.jar.sha256) -split ' ')[0].Trim().ToLower()

# Hash the downloaded jar
$actual = (Get-FileHash Jar\Reactive-paperclip.jar -Algorithm SHA256).Hash.ToLower()

if ($expected -eq $actual) {
    Write-Host "✓ Hash matches — jar is authentic" -ForegroundColor Green
} else {
    Write-Host "✗ Hash mismatch — do NOT run this jar" -ForegroundColor Red
}
```

On Linux/macOS:

```bash
sha256sum -c Reactive-paperclip.jar.sha256
```

## Build your own

If you'd rather build the jar yourself (recommended for production):

```powershell
cd C:\Reactive
./gradlew clean applyAllPatches --no-configuration-cache
./gradlew copyServerJar
```

The freshly built jar will land in this directory.

## Compatibility

`Reactive-paperclip.jar` is a Paper server fork based on upstream Leaf. Run
it the same way as Paper:

```bash
java -jar Reactive-paperclip.jar
```

See [README.md](../README.md) at the repo root for full setup instructions.
