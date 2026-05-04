from __future__ import annotations
import platform
import shutil
import subprocess

REQUIRED = {
    "java": "Java JDK 21",
    "python": "Python 3.11+",
    "gradle": "Gradle 8+",
}


def print_tool(name: str, desc: str) -> None:
    path = shutil.which(name)
    if not path:
        print(f"[MISSING] {name} ({desc})")
        return
    print(f"[OK] {name}: {path}")
    subprocess.run([name, "--version"], check=False)


if __name__ == "__main__":
    print(f"Platform: {platform.platform()}")
    for tool, desc in REQUIRED.items():
        print_tool(tool, desc)
