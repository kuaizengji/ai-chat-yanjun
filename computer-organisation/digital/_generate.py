#!/usr/bin/env python3
"""Generate Digital (hneemann) .dig circuits for the successor / negation assignment."""

from pathlib import Path
from xml.sax.saxutils import escape

OUT = Path(__file__).resolve().parent


def entry(key, value_xml):
    return f"        <entry>\n          <string>{key}</string>\n          {value_xml}\n        </entry>"


def attrs(*pairs):
    if not pairs:
        return "      <elementAttributes/>"
    inner = "\n".join(entry(k, v) for k, v in pairs)
    return f"      <elementAttributes>\n{inner}\n      </elementAttributes>"


def ve(name, x, y, *pairs):
    return (
        "    <visualElement>\n"
        f"      <elementName>{name}</elementName>\n"
        f"{attrs(*pairs)}\n"
        f"      <pos x=\"{x}\" y=\"{y}\"/>\n"
        "    </visualElement>"
    )


def wire(x1, y1, x2, y2):
    return (
        "    <wire>\n"
        f"      <p1 x=\"{x1}\" y=\"{y1}\"/>\n"
        f"      <p2 x=\"{x2}\" y=\"{y2}\"/>\n"
        "    </wire>"
    )


def testdata(s):
    return f"<testData>\n            <dataString>{escape(s)}</dataString>\n          </testData>"


def label(s):
    return ("Label", f"<string>{s}</string>")


def desc(s):
    return ("Description", f"<string>{escape(s)}</string>")


def default_int(n):
    return ("Default", f"<int>{n}</int>")


def font_size(n):
    return ("textFontSize", f"<int>{n}</int>")


def rect_w(n):
    return ("RectWidth", f"<int>{n}</int>")


def rect_h(n):
    return ("RectHeight", f"<int>{n}</int>")


def custom_shape(pins, texts, width=80, height=80, box_label=None):
    pin_xml = []
    for name, px, py in pins:
        pin_xml.append(
            f"""          <entry>
            <string>{name}</string>
            <pin>
              <pos x="{px}" y="{py}"/>
              <showLabel>true</showLabel>
            </pin>
          </entry>"""
        )
    draw = [
        f"""          <poly>
            <poly path="M 0,0 L {width},0 L {width},{height} L 0,{height} Z" evenOdd="false"/>
            <thickness>3</thickness>
            <filled>false</filled>
            <color>
              <red>0</red>
              <green>0</green>
              <blue>0</blue>
              <alpha>255</alpha>
            </color>
          </poly>"""
    ]
    if box_label:
        draw.append(
            f"""          <text>
            <p1 x="{width // 2}" y="{height // 2 + 6}"/>
            <p2 x="{width // 2 + 1}" y="{height // 2 + 6}"/>
            <text>{escape(box_label)}</text>
            <orientation>CENTERBOTTOM</orientation>
            <size>16</size>
            <color>
              <red>0</red>
              <green>0</green>
              <blue>0</blue>
              <alpha>255</alpha>
            </color>
          </text>"""
        )
    for tx, ty, text, ori in texts:
        draw.append(
            f"""          <text>
            <p1 x="{tx}" y="{ty}"/>
            <p2 x="{tx + 1}" y="{ty}"/>
            <text>{escape(text)}</text>
            <orientation>{ori}</orientation>
            <size>14</size>
            <color>
              <red>80</red>
              <green>80</green>
              <blue>80</blue>
              <alpha>255</alpha>
            </color>
          </text>"""
        )
    return f"""        <shape>
        <pins>
{chr(10).join(pin_xml)}
        </pins>
        <drawables>
{chr(10).join(draw)}
        </drawables>
      </shape>"""


def circuit(description, visual, wires, extra_attrs=""):
    return f"""<?xml version="1.0" encoding="utf-8"?>
<circuit>
  <version>1</version>
  <attributes>
    <entry>
      <string>Description</string>
      <string>{escape(description)}</string>
    </entry>
{extra_attrs}  </attributes>
  <visualElements>
{chr(10).join(visual)}
  </visualElements>
  <wires>
{chr(10).join(wires)}
  </wires>
  <measurementOrdering/>
</circuit>
"""


def write_half_adder():
    # Official HalfAdder geometry (known-good) plus a custom shape for nesting.
    shape = custom_shape(
        pins=[("A", 0, 0), ("B", 0, 40), ("S", 80, 0), ("C", 80, 40)],
        texts=[
            (8, 16, "A", "LEFTBOTTOM"),
            (8, 56, "B", "LEFTBOTTOM"),
            (72, 16, "S", "RIGHTBOTTOM"),
            (72, 56, "C", "RIGHTBOTTOM"),
        ],
        width=80,
        height=80,
        box_label="HA",
    )
    extra = f"""    <entry>
      <string>shapeType</string>
      <shapeType>CUSTOM</shapeType>
    </entry>
    <entry>
      <string>Width</string>
      <int>4</int>
    </entry>
    <entry>
      <string>customShape</string>
{shape}
    </entry>
"""
    visual = [
        ve("XOr", 600, 240),
        ve("And", 600, 320),
        ve("In", 540, 240, label("A"), desc("Addend bit A")),
        ve("In", 540, 360, label("B"), desc("Addend bit B")),
        ve("Out", 720, 260, label("S"), desc("Sum bit (A XOR B)")),
        ve("Out", 720, 340, label("C"), desc("Carry bit (A AND B)")),
        ve(
            "Testcase",
            700,
            380,
            label("half-adder-truth"),
            (
                "Testdata",
                testdata(
                    "A B C S\n"
                    "0 0 0 0\n"
                    "0 1 0 1\n"
                    "1 0 0 1\n"
                    "1 1 1 0\n"
                ),
            ),
        ),
        ve(
            "Rectangle",
            520,
            200,
            font_size(18),
            rect_h(12),
            rect_w(12),
            label("Half Adder  S=A⊕B  C=A∧B"),
        ),
    ]
    wires = [
        wire(580, 280, 560, 320),
        wire(560, 280, 580, 320),
        wire(580, 320, 600, 320),
        wire(540, 240, 560, 240),
        wire(560, 240, 600, 240),
        wire(660, 260, 720, 260),
        wire(660, 340, 720, 340),
        wire(580, 280, 600, 280),
        wire(540, 360, 560, 360),
        wire(560, 360, 600, 360),
        wire(560, 240, 560, 280),
        wire(560, 320, 560, 360),
    ]
    text = circuit(
        "Half adder used by the 4-bit successor.\n"
        "半加器：S = A XOR B，C = A AND B。",
        visual,
        wires,
        extra,
    )
    (OUT / "HalfAdder.dig").write_text(text, encoding="utf-8")


def write_successor4():
    # Gate-level 4-bit incrementer.
    # LSB: S0 = NOT A0, C0 = A0
    # Bits 1..3: ripple half adders (XOR + AND)
    shape = custom_shape(
        pins=[
            ("A_0", 0, 0),
            ("A_1", 0, 40),
            ("A_2", 0, 80),
            ("A_3", 0, 120),
            ("S_0", 100, 0),
            ("S_1", 100, 40),
            ("S_2", 100, 80),
            ("S_3", 100, 120),
            ("C_out", 100, 160),
        ],
        texts=[
            (8, 14, "A0", "LEFTBOTTOM"),
            (8, 54, "A1", "LEFTBOTTOM"),
            (8, 94, "A2", "LEFTBOTTOM"),
            (8, 134, "A3", "LEFTBOTTOM"),
            (92, 14, "S0", "RIGHTBOTTOM"),
            (92, 54, "S1", "RIGHTBOTTOM"),
            (92, 94, "S2", "RIGHTBOTTOM"),
            (92, 134, "S3", "RIGHTBOTTOM"),
            (92, 174, "Cout", "RIGHTBOTTOM"),
        ],
        width=100,
        height=180,
        box_label="+1",
    )
    extra = f"""    <entry>
      <string>shapeType</string>
      <shapeType>CUSTOM</shapeType>
    </entry>
    <entry>
      <string>Width</string>
      <int>5</int>
    </entry>
    <entry>
      <string>customShape</string>
{shape}
    </entry>
"""
    visual = [
        ve(
            "Rectangle",
            80,
            80,
            font_size(20),
            rect_h(3),
            rect_w(28),
            label("Exercise 1  Full 4-bit successor  S = A + 1"),
        ),
        # Bit 0
        ve("In", 120, 200, label("A_0"), desc("LSB of the 4-bit input"), default_int(1)),
        ve("Not", 300, 200),
        ve("Out", 720, 200, label("S_0"), desc("LSB of A+1")),
        # Bits 1..3 half adders
        ve("In", 120, 320, label("A_1"), desc("Input bit 1")),
        ve("In", 120, 480, label("A_2"), desc("Input bit 2")),
        ve("In", 120, 640, label("A_3"), desc("MSB of the 4-bit input")),
        ve("Out", 720, 340, label("S_1"), desc("Bit 1 of A+1")),
        ve("Out", 720, 500, label("S_2"), desc("Bit 2 of A+1")),
        ve("Out", 720, 660, label("S_3"), desc("Bit 3 of A+1")),
        ve("Out", 720, 760, label("C_out"), desc("Carry-out: 1111+1 becomes 10000")),
        ve(
            "Rectangle",
            280,
            140,
            font_size(16),
            rect_h(2),
            rect_w(16),
            label("LSB: S0 = NOT A0,  C0 = A0  (HA with +1)"),
        ),
        # Nested half adders: A at (x,y), B at (x,y+40), S at (x+80,y), C at (x+80,y+40)
        ve("HalfAdder.dig", 360, 300),
        ve("HalfAdder.dig", 360, 460),
        ve("HalfAdder.dig", 360, 620),
        ve(
            "Testcase",
            120,
            820,
            label("all-16-inputs"),
            (
                "Testdata",
                testdata(
                    "# All 16 inputs. Full successor includes C_out so 15+1 = 10000.\n"
                    "A_3 A_2 A_1 A_0 C_out S_3 S_2 S_1 S_0\n"
                    "loop(n,16)\n"
                    "bits(4,n) bits(5,n+1)\n"
                    "end loop\n"
                ),
            ),
        ),
    ]

    wires = []
    # A0 -> Not -> S0 ; C0 = A0
    wires += [
        wire(120, 200, 180, 200),
        wire(180, 200, 300, 200),
        wire(340, 200, 720, 200),
        wire(180, 200, 180, 340),
        wire(180, 340, 360, 340),  # C0 -> HA1.B
        # A1 -> HA1.A
        wire(120, 320, 200, 320),
        wire(200, 320, 200, 300),
        wire(200, 300, 360, 300),
        # HA1 S,C
        wire(440, 300, 720, 340),
        wire(440, 340, 500, 340),
        wire(500, 340, 500, 500),
        wire(500, 500, 360, 500),  # C1 -> HA2.B
        # A2 -> HA2.A
        wire(120, 480, 200, 480),
        wire(200, 480, 200, 460),
        wire(200, 460, 360, 460),
        wire(440, 460, 720, 500),
        wire(440, 500, 520, 500),
        wire(520, 500, 520, 660),
        wire(520, 660, 360, 660),  # C2 -> HA3.B
        # A3 -> HA3.A
        wire(120, 640, 200, 640),
        wire(200, 640, 200, 620),
        wire(200, 620, 360, 620),
        wire(440, 620, 720, 660),
        wire(440, 660, 720, 760),  # C_out
    ]

    text = circuit(
        "Full 4-bit successor (incrementer) S = A + 1, with carry-out.\n"
        "完整 4 位后继器：最低位取反，高位用半加器级联进位；1111+1=10000。",
        visual,
        wires,
        extra,
    )
    (OUT / "Successor4.dig").write_text(text, encoding="utf-8")


def half_adder_block(ox, oy, tag):
    """XOR+AND half adder. XOR sits at (ox, oy).

    Pins (same geometry as the tested HalfAdder.dig):
      A -> (ox, oy) and (ox, oy+80)
      B -> (ox, oy+40) and (ox, oy+120)
      S = (ox+60, oy+20)
      C = (ox+60, oy+100)
    """
    gates = [
        ve("XOr", ox, oy),
        ve("And", ox, oy + 80),
        ve(
            "Rectangle",
            ox - 20,
            oy - 40,
            font_size(14),
            rect_h(1),
            rect_w(8),
            label(tag),
        ),
    ]
    return gates


def write_negation3():
    # One file, gate level. No HalfAdder.dig / Successor4.dig dependency.
    # Stage 1: zero-extend X to 4 bits and invert.
    # Stage 2: full 4-bit successor inlined (LSB NOT, then three XOR/AND half adders).
    #
    # Not: input at pos, output at (x+40, y).
    # XOR/AND: see half_adder_block.
    shape = custom_shape(
        pins=[
            ("X_0", 0, 0),
            ("X_1", 0, 40),
            ("X_2", 0, 80),
            ("Y_0", 100, 0),
            ("Y_1", 100, 40),
            ("Y_2", 100, 80),
            ("Y_3", 100, 120),
        ],
        texts=[
            (8, 14, "X0", "LEFTBOTTOM"),
            (8, 54, "X1", "LEFTBOTTOM"),
            (8, 94, "X2", "LEFTBOTTOM"),
            (92, 14, "Y0", "RIGHTBOTTOM"),
            (92, 54, "Y1", "RIGHTBOTTOM"),
            (92, 94, "Y2", "RIGHTBOTTOM"),
            (92, 134, "Y3", "RIGHTBOTTOM"),
        ],
        width=100,
        height=140,
        box_label="-X",
    )
    extra = f"""    <entry>
      <string>shapeType</string>
      <shapeType>CUSTOM</shapeType>
    </entry>
    <entry>
      <string>Width</string>
      <int>5</int>
    </entry>
    <entry>
      <string>customShape</string>
{shape}
    </entry>
"""
    # Half adders for bits 1..3. XOR origin (ox, oy).
    ha = [(560, 280), (560, 560), (560, 840)]
    visual = [
        ve(
            "Rectangle",
            60,
            20,
            font_size(18),
            rect_h(2),
            rect_w(36),
            label("Exercise 2  3-bit negation, all gates in this one file   -X = ~X + 1"),
        ),
        ve(
            "Rectangle",
            60,
            80,
            font_size(14),
            rect_h(2),
            rect_w(14),
            label("1. zero-extend to 4 bits, then invert"),
        ),
        ve("In", 80, 160, label("X_0"), desc("LSB of the unsigned 3-bit input"), default_int(1)),
        ve("In", 80, 280, label("X_1"), desc("Input bit 1")),
        ve("In", 80, 560, label("X_2"), desc("MSB of the unsigned 3-bit input")),
        ve(
            "Const",
            80,
            840,
            ("Value", "<long>0</long>"),
            desc("Zero-extend: the new high bit is constant 0, then inverted to 1"),
        ),
        ve("Not", 200, 160),
        ve("Not", 200, 280),
        ve("Not", 200, 560),
        ve("Not", 200, 840),
        ve(
            "Rectangle",
            380,
            80,
            font_size(14),
            rect_h(2),
            rect_w(18),
            label("2. inlined 4-bit successor (+1)"),
        ),
        ve(
            "Rectangle",
            360,
            120,
            font_size(14),
            rect_h(1),
            rect_w(12),
            label("bit0: S0 = NOT B0, C0 = B0"),
        ),
        ve("Not", 400, 160),
        *half_adder_block(ha[0][0], ha[0][1], "bit1 half adder  S=A xor C, C=A and C"),
        *half_adder_block(ha[1][0], ha[1][1], "bit2 half adder"),
        *half_adder_block(ha[2][0], ha[2][1], "bit3 half adder"),
        ve("Out", 1040, 160, label("Y_0"), desc("LSB of -X in 4-bit two's complement")),
        ve("Out", 1040, 300, label("Y_1")),
        ve("Out", 1040, 580, label("Y_2")),
        ve("Out", 1040, 860, label("Y_3"), desc("Sign bit of -X")),
        ve(
            "Testcase",
            80,
            1040,
            label("all-8-inputs"),
            (
                "Testdata",
                testdata(
                    "# All 8 inputs. Y is the 4-bit two's complement of -X.\n"
                    "# 001 -> 1111 (-1); 010 -> 1110 (-2); 000 -> 0000; 111 -> 1001 (-7).\n"
                    "X_2 X_1 X_0 Y_3 Y_2 Y_1 Y_0\n"
                    "loop(n,8)\n"
                    "bits(3,n) bits(4,16-n)\n"
                    "end loop\n"
                ),
            ),
        ),
    ]

    def ha_wires(ox, oy, ax, ay, cin_x, cin_entry_y):
        """Connect inverted bit (ax, ay) and carry-in rail to one half adder.

        Carry-in arrives on a vertical rail at x=cin_x, then turns right at the B pins.
        Data bit stays on its own y and never shares a vertex with that rail.
        """
        fan = ox - 60
        return [
            # A fanout: XOR in (ox, oy), AND in (ox, oy+80)
            wire(ax, ay, fan, ay),
            wire(fan, ay, ox, oy),
            wire(fan, ay, fan, oy + 80),
            wire(fan, oy + 80, ox, oy + 80),
            # B / carry-in: XOR in (ox, oy+40), AND in (ox, oy+120)
            wire(cin_x, cin_entry_y, cin_x, oy + 40),
            wire(cin_x, oy + 40, ox, oy + 40),
            wire(cin_x, oy + 40, cin_x, oy + 120),
            wire(cin_x, oy + 120, ox, oy + 120),
        ]

    wires = [
        # Invert the zero-extended input. Not output is x+40.
        wire(80, 160, 200, 160),
        wire(80, 280, 200, 280),
        wire(80, 560, 200, 560),
        wire(80, 840, 200, 840),
        # B0 = ~X0 into the bit0 NOT; S0 = ~B0 = Y0.
        wire(240, 160, 400, 160),
        wire(440, 160, 1040, 160),
        # C0 = B0 travels above the inputs, then down the far-left rail.
        wire(240, 160, 240, 40),
        wire(240, 40, 40, 40),
        wire(40, 40, 40, 320),
    ]
    # bit1 A is ~X1 at (240, 280); carry rail x=40 enters at y=320 (XOR B pin).
    wires += ha_wires(560, 280, 240, 280, 40, 320)
    # C1 leaves AND at (620, 380), ducks into the gap, returns on the left rail.
    wires += [
        wire(620, 380, 700, 380),
        wire(700, 380, 700, 480),
        wire(700, 480, 40, 480),
        wire(40, 480, 40, 600),
    ]
    wires += ha_wires(560, 560, 240, 560, 40, 600)
    # Sums to the right. y = oy+20, so they sit between the A and B rows.
    wires += [
        wire(620, 300, 1040, 300),
        wire(620, 580, 1040, 580),
    ]
    # C2 leaves AND at (620, 660).
    wires += [
        wire(620, 660, 760, 660),
        wire(760, 660, 760, 760),
        wire(760, 760, 40, 760),
        wire(40, 760, 40, 880),
    ]
    wires += ha_wires(560, 840, 240, 840, 40, 880)
    wires += [
        wire(620, 860, 1040, 860),
        # C3 (620, 940) is the successor carry-out. For X=0 it is 1 and the
        # low 4 bits are 0000, so this pin is intentionally left open.
    ]

    text = circuit(
        "Exercise 2 in a single file: 3-bit unsigned to 4-bit two's complement.\n"
        "-X = ~X + 1. Zero-extend, invert, then the full 4-bit successor (NOT + three half adders) is drawn with gates in this same circuit. No other .dig file is required.",
        visual,
        wires,
        extra,
    )
    (OUT / "Negation3.dig").write_text(text, encoding="utf-8")


if __name__ == "__main__":
    write_half_adder()
    write_successor4()
    write_negation3()
    print("wrote", OUT / "HalfAdder.dig")
    print("wrote", OUT / "Successor4.dig")
    print("wrote", OUT / "Negation3.dig")
