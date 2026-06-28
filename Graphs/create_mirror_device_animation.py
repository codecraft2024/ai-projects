#!/usr/bin/env python3
"""Generate animated GIF for Mirror Device linking sequence diagram."""

from __future__ import annotations

import math
from pathlib import Path

import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyArrowPatch, FancyBboxPatch
import numpy as np
from PIL import Image

OUTPUT = Path(__file__).parent / "scenario1_mirror_device_linking.gif"
DPI = 100
FIG_W, FIG_H = 10.24, 6.31

# Colors
PURPLE = "#6B2D8C"
BLUE = "#1E5AA8"
LIGHT_BLUE = "#4A90D9"
RED = "#D32F2F"
GREEN = "#2E7D32"
GRAY = "#555555"
BG = "#FFFFFF"

STEPS = [
    "1. get operator(s) Info",
    "2. Fetch User identification Data encrypted (ts+deviceId+userId)",
    "3. Resolve Device Data to mobile number",
    "4. new call: Get User Accounts / IPA's (masked)",
    "5. new call: User Authentication for New Mirror Device Enrollment",
    "6. Open Home screen - do normal API calls with Header isMirrorDevice=true",
]


def rounded_box(ax, xy, w, h, label, fontsize=11, lw=+2):
    box = FancyBboxPatch(
        xy,
        w,
        h,
        boxstyle="round,pad=0.02,rounding_size=0.08",
        linewidth=lw,
        edgecolor=BLUE,
        facecolor="white",
        zorder=2,
    )
    ax.add_patch(box)
    ax.text(
        xy[0] + w / 2,
        xy[1] + h / 2,
        label,
        ha="center",
        va="center",
        fontsize=fontsize,
        fontweight="bold",
        color=BLUE,
        zorder=3,
    )
    return box


def dashed_device(ax, xy, w, h, title):
    box = FancyBboxPatch(
        xy,
        w,
        h,
        boxstyle="round,pad=0.02,rounding_size=0.06",
        linewidth=2,
        edgecolor=BLUE,
        facecolor="#F8FBFF",
        linestyle="--",
        zorder=1,
    )
    ax.add_patch(box)
    ax.text(xy[0] + w / 2, xy[1] + h - 0.18, title, ha="center", va="top", fontsize=10, color=GRAY, zorder=3)
    return box


def phone_icon(ax, cx, cy, label="INSTAPAY"):
    phone = FancyBboxPatch(
        (cx - 0.35, cy - 0.55),
        0.7,
        1.1,
        boxstyle="round,pad=0.01,rounding_size=0.05",
        linewidth=1.5,
        edgecolor="#333",
        facecolor="#111",
        zorder=4,
    )
    ax.add_patch(phone)
    screen = FancyBboxPatch(
        (cx - 0.28, cy - 0.38),
        0.56,
        0.75,
        boxstyle="round,pad=0.01,rounding_size=0.03",
        linewidth=0,
        facecolor="white",
        zorder=5,
    )
    ax.add_patch(screen)
    ax.text(cx, cy - 0.02, label, ha="center", va="center", fontsize=7, fontweight="bold", color=PURPLE, zorder=6)


def arrow(ax, start, end, style="-|>", color=BLUE, lw=2, alpha=1.0, zorder=10, mutation=12):
    arr = FancyArrowPatch(
        start,
        end,
        arrowstyle=style,
        mutation_scale=mutation,
        linewidth=lw,
        color=color,
        alpha=alpha,
        zorder=zorder,
        connectionstyle="arc3,rad=0",
    )
    ax.add_patch(arr)
    return arr


def bidirectional(ax, y, x1, x2, color=BLUE, lw=2, alpha=1.0, zorder=10):
    arrow(ax, (x1, y), (x2, y), style="<|-|>", color=color, lw=lw, alpha=alpha, zorder=zorder)


def step_badge(ax, x, y, num, active=False):
    r = 0.14
    face = RED if active else "#FFCDD2"
    edge = RED
    lw = 2.5 if active else 1.2
    circle = plt.Circle((x, y), r, facecolor=face, edgecolor=edge, linewidth=lw, zorder=20)
    ax.add_patch(circle)
    ax.text(x, y, str(num), ha="center", va="center", fontsize=9, fontweight="bold", color="white", zorder=21)


def draw_static(ax):
    ax.set_xlim(0, FIG_W)
    ax.set_ylim(0, FIG_H)
    ax.set_aspect("equal")
    ax.axis("off")
    ax.set_facecolor(BG)

    ax.text(
        FIG_W / 2,
        FIG_H - 0.35,
        "Scenario 1: Linking a New Mirror Device to an Active User Account",
        ha="center",
        va="top",
        fontsize=13,
        fontweight="bold",
        color=PURPLE,
    )

    # Mirror device (left)
    dashed_device(ax, (0.55, 1.55), 2.0, 3.55, "Mirror Device")
    phone_icon(ax, 1.55, 3.35)
    rounded_box(ax, (0.95, 1.75), 1.2, 0.55, "SDK", fontsize=10)

    # Master device (right)
    dashed_device(ax, (7.7, 1.55), 2.0, 3.55, "Master Device")
    phone_icon(ax, 8.7, 3.35)

    # Backend stack
    rounded_box(ax, (3.55, 4.55), 3.1, 0.65, "Mobile Server")
    rounded_box(ax, (3.55, 3.55), 3.1, 0.65, "PSP")
    rounded_box(ax, (3.15, 2.35), 3.9, 0.75, "New Backend Service\n(Mapping Table)", fontsize=9)

    # Legend background
    legend_box = FancyBboxPatch(
        (0.35, 0.08),
        9.5,
        1.15,
        boxstyle="round,pad=0.02,rounding_size=0.05",
        linewidth=1.5,
        edgecolor="#CCCCCC",
        facecolor="#FAFAFA",
        zorder=0,
    )
    ax.add_patch(legend_box)

    for i, text in enumerate(STEPS):
        y = 1.02 - i * 0.17
        ax.text(0.55, y, text, ha="left", va="center", fontsize=7.2, color=GRAY, zorder=1)


def draw_arrows_for_step(ax, step: int, pulse: float = 1.0):
    """Draw arrows for steps 1..step with current step highlighted."""
    app_x = 1.55
    sdk_y = 2.05
    app_y = 3.0
    server_x = 5.1
    mobile_y = 4.87
    psp_y = 3.87
    backend_y = 2.72

    alpha_base = 0.25
    lw_base = 1.5
    highlight_lw = 2.5 + 1.5 * pulse
    highlight_alpha = 0.85 + 0.15 * pulse

    # Step 1: app -> SDK
    if step >= 1:
        active = step == 1
        arrow(
            ax,
            (app_x, app_y - 0.5),
            (app_x, sdk_y + 0.55),
            color=LIGHT_BLUE if active else BLUE,
            lw=highlight_lw if active else lw_base,
            alpha=highlight_alpha if active else alpha_base,
        )
        step_badge(ax, app_x + 0.55, (app_y - 0.5 + sdk_y + 0.55) / 2, 1, active=active)

    # Step 2: SDK -> app
    if step >= 2:
        active = step == 2
        arrow(
            ax,
            (app_x - 0.15, sdk_y + 0.55),
            (app_x - 0.15, app_y - 0.5),
            color=LIGHT_BLUE if active else BLUE,
            lw=highlight_lw if active else lw_base,
            alpha=highlight_alpha if active else alpha_base,
        )
        step_badge(ax, app_x - 0.7, (app_y - 0.5 + sdk_y + 0.55) / 2, 2, active=active)

    # Step 3: app -> mobile server, mobile <-> PSP, PSP <-> backend
    if step >= 3:
        active = step == 3
        c = LIGHT_BLUE if active else BLUE
        lw = highlight_lw if active else lw_base
        a = highlight_alpha if active else alpha_base

        arrow(ax, (app_x + 0.35, app_y), (server_x - 1.55, mobile_y), color=c, lw=lw, alpha=a)
        bidirectional(ax, 4.35, 4.0, 6.2, color=c, lw=lw, alpha=a)
        bidirectional(ax, 3.35, 4.0, 6.2, color=c, lw=lw, alpha=a)
        step_badge(ax, 3.0, 4.35, 3, active=active)

    # Step 4: app -> mobile server
    if step >= 4:
        active = step == 4
        arrow(
            ax,
            (app_x + 0.35, app_y + 0.15),
            (server_x - 1.55, mobile_y - 0.1),
            color=LIGHT_BLUE if active else BLUE,
            lw=highlight_lw if active else lw_base,
            alpha=highlight_alpha if active else alpha_base,
        )
        step_badge(ax, 3.2, 4.1, 4, active=active)

    # Step 5: mobile server -> app
    if step >= 5:
        active = step == 5
        arrow(
            ax,
            (server_x - 1.55, mobile_y - 0.25),
            (app_x + 0.35, app_y - 0.15),
            color=LIGHT_BLUE if active else BLUE,
            lw=highlight_lw if active else lw_base,
            alpha=highlight_alpha if active else alpha_base,
        )
        step_badge(ax, 3.2, 3.9, 5, active=active)

    # Step 6: success state on mirror device
    if step >= 6:
        active = step == 6
        glow = FancyBboxPatch(
            (0.55, 1.55),
            2.0,
            3.55,
            boxstyle="round,pad=0.02,rounding_size=0.06",
            linewidth=3 + pulse,
            edgecolor=GREEN if active else "#A5D6A7",
            facecolor="none",
            linestyle="--",
            zorder=8,
            alpha=0.6 + 0.4 * pulse if active else 0.4,
        )
        ax.add_patch(glow)
        if active:
            ax.text(
                1.55,
                4.25,
                "Linked",
                ha="center",
                va="center",
                fontsize=11,
                fontweight="bold",
                color=GREEN,
                zorder=25,
                bbox=dict(boxstyle="round,pad=0.3", facecolor="#E8F5E9", edgecolor=GREEN, alpha=0.95),
            )


def highlight_legend(ax, step: int, pulse: float = 1.0):
    y_positions = [1.02 - i * 0.17 for i in range(6)]
    for i in range(6):
        y = y_positions[i]
        if i + 1 == step:
            highlight = FancyBboxPatch(
                (0.45, y - 0.08),
                9.2,
                0.16,
                boxstyle="round,pad=0.01,rounding_size=0.02",
                linewidth=0,
                facecolor="#FFF3E0" if i < 5 else "#E8F5E9",
                edgecolor=RED if i < 5 else GREEN,
                alpha=0.5 + 0.4 * pulse,
                zorder=0.5,
            )
            ax.add_patch(highlight)
            ax.text(
                0.55,
                y,
                STEPS[i],
                ha="left",
                va="center",
                fontsize=7.5,
                color=RED if i < 5 else GREEN,
                fontweight="bold",
                zorder=2,
            )
        elif i + 1 < step:
            ax.text(0.55, y, STEPS[i], ha="left", va="center", fontsize=7.2, color="#888888", zorder=1)


def render_frame(step: int, pulse: float = 1.0) -> np.ndarray:
    fig, ax = plt.subplots(figsize=(FIG_W, FIG_H), dpi=DPI)
    draw_static(ax)
    if step > 0:
        draw_arrows_for_step(ax, step, pulse=pulse)
        highlight_legend(ax, step, pulse=pulse)
    fig.canvas.draw()
    buf = np.asarray(fig.canvas.buffer_rgba())
    plt.close(fig)
    return buf[:, :, :3]


def build_gif():
    frames: list[Image.Image] = []
    fps = 10

    # Intro: show diagram without arrows
    for _ in range(fps * 1):
        frames.append(Image.fromarray(render_frame(0)))

    # Animate each step
    for step in range(1, 7):
        hold_frames = fps * 2 if step < 6 else fps * 3
        pulse_frames = 8
        for f in range(hold_frames):
            pulse = 0.5 + 0.5 * math.sin(2 * math.pi * (f % pulse_frames) / pulse_frames)
            frames.append(Image.fromarray(render_frame(step, pulse=pulse)))

    # Final hold with all arrows visible
    for step in range(1, 7):
        for _ in range(3):
            frames.append(Image.fromarray(render_frame(step, pulse=1.0)))

    final = render_frame(6, pulse=1.0)
    for _ in range(fps * 2):
        frames.append(Image.fromarray(final))

    duration_ms = int(1000 / fps)
    frames[0].save(
        OUTPUT,
        save_all=True,
        append_images=frames[1:],
        duration=duration_ms,
        loop=0,
        optimize=True,
    )
    print(f"Saved animated GIF: {OUTPUT} ({len(frames)} frames, {len(frames)*duration_ms/1000:.1f}s)")


if __name__ == "__main__":
    build_gif()
