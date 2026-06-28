#!/usr/bin/env python3
"""Animate the original sequence diagram by highlighting steps in order."""

from __future__ import annotations

import math
from pathlib import Path

from PIL import Image, ImageDraw, ImageEnhance, ImageFont

ASSET = Path(
    "/Users/minaanwer/.cursor/projects/Users-minaanwer-Desktop-githup-repo-ai-projects-ai-projects-Graphs/assets/image-0faff2c9-6fb2-4edd-9895-c781debb2862.png"
)
OUTPUT = Path(__file__).parent / "scenario1_mirror_device_linking.gif"

W, H = 1024, 631
HIGHLIGHT = (255, 87, 34)
GREEN = (46, 125, 50)
RED = (211, 47, 47)
GLOW = (255, 193, 7)

STEPS = [
    "1. get operator(s) Info",
    "2. Fetch User identification Data encrypted (ts+deviceId+userId)",
    "3. Resolve Device Data to mobile number",
    "4. new call: Get User Accounts / IPA's (masked)",
    "5. new call: User Authentication for New Mirror Device Enrollment",
    "6. Open Home screen - do normal API calls with Header isMirrorDevice=true",
]

LEGEND_Y = [498, 518, 538, 558, 578, 598]

# Glow regions aligned to existing arrows in the source image
STEP_GLOWS = {
    1: [(145, 300, 195, 390)],
    2: [(130, 300, 180, 390)],
    3: [(190, 90, 640, 250)],
    4: [(195, 95, 420, 220)],
    5: [(190, 95, 420, 210)],
}


def load_font(size: int):
    for name in (
        "/System/Library/Fonts/Supplemental/Arial Bold.ttf",
        "/System/Library/Fonts/Helvetica.ttc",
        "/Library/Fonts/Arial.ttf",
    ):
        try:
            return ImageFont.truetype(name, size)
        except OSError:
            continue
    return ImageFont.load_default()


def pulse_badge(draw: ImageDraw.ImageDraw, x: int, y: int, num: int, pulse: float):
    r = int(13 + 4 * pulse)
    draw.ellipse((x - r, y - r, x + r, y + r), fill=RED, outline=(180, 0, 0), width=2)
    draw.text((x, y), str(num), fill="white", font=load_font(13), anchor="mm")


def render_frame(base: Image.Image, step: int, pulse: float) -> Image.Image:
    frame = base.copy().convert("RGBA")
    overlay = Image.new("RGBA", (W, H), (0, 0, 0, 0))
    d = ImageDraw.Draw(overlay)
    bold = load_font(12)
    normal = load_font(11)

    # Dim completed legend rows; highlight active row
    for i, y in enumerate(LEGEND_Y):
        s = i + 1
        if s < step:
            d.rectangle((28, y - 9, 995, y + 9), fill=(220, 220, 220, 60))
            d.text((38, y), STEPS[i], fill=(120, 120, 120), font=normal, anchor="lm")
        elif s == step:
            alpha = int(130 + 90 * pulse)
            fill = (255, 243, 224, alpha) if s < 6 else (232, 245, 233, alpha)
            border = HIGHLIGHT if s < 6 else GREEN
            d.rectangle((28, y - 9, 995, y + 9), fill=fill, outline=border, width=2)
            d.text((38, y), STEPS[i], fill=border, font=bold, anchor="lm")

    # Glow over active arrow region
    if 1 <= step <= 5:
        for box in STEP_GLOWS[step]:
            x1, y1, x2, y2 = box
            alpha = int(70 + 90 * pulse)
            d.rounded_rectangle((x1, y1, x2, y2), radius=10, fill=(*GLOW, alpha))

    badge_pos = {1: (215, 335), 2: (125, 335), 3: (300, 145), 4: (310, 110), 5: (310, 95)}
    if step in badge_pos:
        pulse_badge(d, *badge_pos[step], step, pulse)

    if step >= 6:
        alpha = int(90 + 70 * pulse)
        d.rounded_rectangle((78, 88, 252, 448), radius=12, outline=(*GREEN, alpha), width=4)
        d.rounded_rectangle((108, 102, 222, 148), radius=8, fill=(232, 245, 233, 230), outline=GREEN, width=2)
        d.text((165, 125), "Linked", fill=GREEN, font=load_font(16), anchor="mm")

    composed = Image.alpha_composite(frame, overlay)
    if step > 0:
        composed = ImageEnhance.Brightness(composed).enhance(1.0 + 0.03 * pulse)
    return composed.convert("RGB")


def build_gif():
    base = Image.open(ASSET).convert("RGB")
    frames: list[Image.Image] = []
    fps = 10

    for _ in range(fps):
        frames.append(render_frame(base, 0, 0))

    for step in range(1, 7):
        hold = fps * 2 if step < 6 else fps * 3
        for f in range(hold):
            pulse = 0.5 + 0.5 * math.sin(2 * math.pi * (f % 8) / 8)
            frames.append(render_frame(base, step, pulse))

    for step in range(1, 7):
        frames.append(render_frame(base, step, 1.0))

    for _ in range(fps * 2):
        frames.append(render_frame(base, 6, 1.0))

    duration = int(1000 / fps)
    frames[0].save(
        OUTPUT,
        save_all=True,
        append_images=frames[1:],
        duration=duration,
        loop=0,
        optimize=True,
    )
    print(f"Saved: {OUTPUT} ({len(frames)} frames, {len(frames) * duration / 1000:.1f}s)")


if __name__ == "__main__":
    build_gif()
