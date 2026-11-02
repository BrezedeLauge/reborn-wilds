#!/usr/bin/env python3
"""Generate the original tiny ambient sprites using only the Python standard library.

These geometric pixel sprites were authored for this project. No Minecraft,
Dungeons, Actions & Stuff or other third-party image is read or transformed.
Run from any directory: python3 tools/generate_ambient_textures.py
"""

from pathlib import Path
import struct
import zlib

ROOT = Path(__file__).resolve().parents[1]
OUTPUT = ROOT / "src/main/resources/assets/dungeonsalive/textures/particle"
SIZE = 16


def png(path: Path, pixels: list[list[tuple[int, int, int, int]]]) -> None:
    def chunk(kind: bytes, payload: bytes) -> bytes:
        return (struct.pack(">I", len(payload)) + kind + payload
                + struct.pack(">I", zlib.crc32(kind + payload) & 0xFFFFFFFF))
    scanlines = b"".join(b"\0" + bytes(channel for pixel in row for channel in pixel)
                         for row in pixels)
    image = (b"\x89PNG\r\n\x1a\n"
             + chunk(b"IHDR", struct.pack(">IIBBBBB", SIZE, SIZE, 8, 6, 0, 0, 0))
             + chunk(b"IDAT", zlib.compress(scanlines, 9)) + chunk(b"IEND", b""))
    path.write_bytes(image)


def canvas() -> list[list[tuple[int, int, int, int]]]:
    return [[(0, 0, 0, 0) for _ in range(SIZE)] for _ in range(SIZE)]


def butterfly(width: int) -> list[list[tuple[int, int, int, int]]]:
    pixels = canvas()
    # Symmetrical upper/lower wings; a near-white palette supports in-game tint.
    for y in range(3, 13):
        wing_width = width if y < 8 else max(1, width - 1)
        if y in (3, 12):
            wing_width = max(1, wing_width - 2)
        for direction in (-1, 1):
            for offset in range(1, wing_width + 1):
                x = 7 + offset * direction + (1 if direction > 0 else 0)
                edge = offset == wing_width or y in (3, 12)
                colour = (61, 55, 47, 255) if edge else (245, 240, 226, 255)
                if not edge and (y + offset) % 5 == 0:
                    colour = (183, 175, 157, 255)
                pixels[y][x] = colour
    for y in range(4, 12):
        pixels[y][7] = (50, 47, 43, 255)
        pixels[y][8] = (69, 61, 51, 255)
    pixels[2][6] = pixels[2][9] = (74, 69, 55, 255)
    pixels[3][7] = pixels[3][8] = (74, 69, 55, 255)
    return pixels


def firefly() -> list[list[tuple[int, int, int, int]]]:
    pixels = canvas()
    for y in range(SIZE):
        for x in range(SIZE):
            distance = ((x - 7.5) ** 2 + (y - 8.5) ** 2) ** 0.5
            if distance < 6:
                pixels[y][x] = (191, 234, 93, int(85 * (1 - distance / 6) ** 2))
    for x, y in ((6, 6), (5, 5), (9, 6), (10, 5)):
        pixels[y][x] = (185, 216, 162, 135)
    for y in (6, 7):
        pixels[y][7] = pixels[y][8] = (60, 83, 42, 255)
    for y in (8, 9):
        pixels[y][7] = pixels[y][8] = (235, 255, 140, 255)
    return pixels


def spore() -> list[list[tuple[int, int, int, int]]]:
    pixels = canvas()
    for y in range(SIZE):
        for x in range(SIZE):
            distance = ((x - 7.5) ** 2 + (y - 7.5) ** 2) ** 0.5
            if distance < 3.4:
                pixels[y][x] = (237, 233, 201, int(240 * (1 - distance / 3.4)))
    return pixels


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    textures = {"butterfly_open": butterfly(5), "butterfly_half": butterfly(3),
                "butterfly_closed": butterfly(1), "firefly": firefly(), "spore": spore()}
    for name, pixels in textures.items():
        png(OUTPUT / f"{name}.png", pixels)
    print(f"Generated {len(textures)} original ambient sprites in {OUTPUT}")


if __name__ == "__main__":
    main()
