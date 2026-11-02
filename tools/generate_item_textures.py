#!/usr/bin/env python3
"""Draw an original wind-horn pixel icon without reading third-party artwork.

The output is a reproducible 32x32 RGBA PNG. Python standard library only.
"""

from pathlib import Path
import struct
import zlib

SIZE = 32
OUTPUT = (Path(__file__).resolve().parents[1]
          / "src/main/resources/assets/dungeonsalive/textures/item/wind_horn.png")


def main() -> None:
    pixels = [[(0, 0, 0, 0) for _ in range(SIZE)] for _ in range(SIZE)]

    def polygon(points: list[tuple[int, int]], colour: tuple[int, int, int, int]) -> None:
        for y in range(SIZE):
            for x in range(SIZE):
                px, py, inside = x + 0.5, y + 0.5, False
                last_x, last_y = points[-1]
                for current_x, current_y in points:
                    if ((current_y > py) != (last_y > py)
                            and px < (last_x - current_x) * (py - current_y)
                            / (last_y - current_y) + current_x):
                        inside = not inside
                    last_x, last_y = current_x, current_y
                if inside:
                    pixels[y][x] = colour

    polygon([(5, 23), (7, 23), (9, 21), (10, 18), (10, 15), (12, 12),
             (16, 10), (20, 8), (22, 4), (26, 4), (29, 7), (29, 12),
             (26, 16), (22, 16), (21, 14), (17, 14), (15, 16),
             (14, 20), (12, 24), (8, 27), (5, 26)], (55, 43, 35, 255))
    polygon([(6, 24), (8, 24), (11, 21), (12, 17), (14, 14),
             (18, 12), (23, 11), (24, 7), (22, 7), (20, 10),
             (16, 11), (12, 14), (11, 18), (10, 22), (7, 25)], (165, 135, 86, 255))
    polygon([(7, 24), (10, 23), (13, 19), (14, 15), (17, 13),
             (23, 13), (22, 10), (17, 11), (13, 14), (12, 18),
             (10, 21)], (226, 203, 147, 255))
    polygon([(11, 20), (12, 16), (15, 13), (20, 11), (20, 12),
             (16, 14), (14, 16), (13, 20)], (250, 233, 184, 255))
    # Copper bell with a dark interior, deliberately different from the Dungeons art.
    polygon([(22, 5), (25, 5), (28, 8), (28, 12), (25, 15),
             (23, 15), (21, 11)], (180, 99, 61, 255))
    polygon([(23, 6), (25, 6), (27, 8), (27, 12), (25, 14),
             (24, 14), (22, 10)], (230, 158, 93, 255))
    polygon([(24, 7), (26, 8), (27, 10), (26, 12), (25, 13),
             (24, 11), (23, 9)], (61, 48, 41, 255))
    polygon([(24, 8), (26, 9), (26, 11), (25, 12), (24, 10)], (105, 68, 47, 255))
    # Leather grip and a small turquoise inlay.
    polygon([(15, 11), (18, 10), (20, 14), (17, 15)], (91, 64, 47, 255))
    polygon([(16, 11), (17, 11), (19, 14), (18, 14)], (182, 125, 67, 255))
    pixels[12][17] = (104, 211, 188, 255)
    pixels[13][18] = (54, 145, 131, 255)

    raw = b"".join(b"\0" + bytes(value for pixel in row for value in pixel) for row in pixels)

    def chunk(kind: bytes, payload: bytes) -> bytes:
        return (struct.pack(">I", len(payload)) + kind + payload
                + struct.pack(">I", zlib.crc32(kind + payload) & 0xFFFFFFFF))

    data = (b"\x89PNG\r\n\x1a\n"
            + chunk(b"IHDR", struct.pack(">IIBBBBB", SIZE, SIZE, 8, 6, 0, 0, 0))
            + chunk(b"IDAT", zlib.compress(raw, 9)) + chunk(b"IEND", b""))
    OUTPUT.parent.mkdir(parents=True, exist_ok=True)
    OUTPUT.write_bytes(data)
    print(f"Generated original wind-horn sprite: {OUTPUT}")


if __name__ == "__main__":
    main()
