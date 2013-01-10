# FakeTileAO
*Fake ambient occlusion lightmap generator for tile maps*

Contents:

1. What is FakeTileAO?
2. How does it work?
3. Usage
4. License and copyright

## 1. What is FakeTileAO?

This is a fake ambient occlusion lightmap generator for tile maps. It works only for simple square tile maps with flat walls. The generated textures can be used to give the tile map a bit smoother, more realistic look.

## 2. How does it work?

A unique lightmap needs to be generated for every possible layout. As each tile has 8 adjacent tiles around it that could contribute to the AO, it makes it 256 different textures. As it is quite a lot, we have to cut it down a bit:

- Many of these textures are identical if *rotated by 90, 180 or 270 degrees*. Not duplicating them leaves us with 70 different textures to generate.
- If we take *simple mirroring* into account, we're down to 51.
- If we assume that a wall to the top and to the left completely seals the top-left corner, regardless whether there is a wall tile to the top-left, we brought it down to 33.

We could save more memory by further dropping matching textures which can be built from others, but this way we can save GPU rendering passes by allowing it to be used with single-pass multitexturing. AO maps are also quite light and subtle gradients, low resolution textures can be used for tile maps. When using the generated textures, you will need to deal with the above transformations yourself.

Generating the maps is simple: we just draw the 9 required tiles in gray and white, apply some gaussian blur, and save the middle tile.

For more information and the results, read the article on [CodeBin](http://codebin.co.uk/blog/fake-ambient-occlusion-part1/).

## 3. Usage

It is first and foremost a development tool, not very user friendly, and once compiled, fully automatic. To change the parameters, edit the static fields of **org.codebin.faketileao.Config**.

You can run the program from within Eclipse or from the terminal. It creates a *generated* folder where it puts all the generated PNG files. The filenames are the layout indices.

As the tile in the middle is always empty, there are 8 adjacent tiles which are either walls (1) or blanks (0). This makes each layout representable by an 8-bit number, which is the layout index. The lowest bit represents the top-left corner, and it goes clockwise. Mirroring is done horizontally, by swapping the following bit pairs: 1-3, 4-8, 5-7.

The layouts and their associated tiles:

```
0 is GENERATED.
1 is GENERATED.
2 is GENERATED.
3 is GENERATED.
4 is the same as 1 ROTATE_90.
5 is GENERATED.
6 is the same as 3 MIRROR.
7 is GENERATED.
8 is the same as 2 ROTATE_90.
9 is GENERATED.
10 is the same as 14 IDENTICAL.
11 is the same as 15 IDENTICAL.
12 is the same as 3 ROTATE_90.
13 is GENERATED.
14 is GENERATED.
15 is GENERATED.
16 is the same as 1 ROTATE_180.
17 is GENERATED.
18 is the same as 9 MIRROR_90.
19 is GENERATED.
20 is the same as 5 ROTATE_90.
21 is GENERATED.
22 is the same as 13 MIRROR_90.
23 is GENERATED.
24 is the same as 3 MIRROR_90.
25 is the same as 19 MIRROR_90.
26 is the same as 15 MIRROR_90.
27 is the same as 31 IDENTICAL.
28 is the same as 7 ROTATE_90.
29 is the same as 23 MIRROR_90.
30 is the same as 15 MIRROR_90.
31 is GENERATED.
32 is the same as 2 ROTATE_180.
33 is the same as 9 MIRROR_270.
34 is GENERATED.
35 is GENERATED.
36 is the same as 9 ROTATE_90.
37 is GENERATED.
38 is the same as 35 MIRROR.
39 is GENERATED.
40 is the same as 14 ROTATE_90.
41 is the same as 57 IDENTICAL.
42 is the same as 62 IDENTICAL.
43 is the same as 63 IDENTICAL.
44 is the same as 15 ROTATE_90.
45 is the same as 61 IDENTICAL.
46 is the same as 62 IDENTICAL.
47 is the same as 63 IDENTICAL.
48 is the same as 3 ROTATE_180.
49 is the same as 19 ROTATE_180.
50 is the same as 35 ROTATE_180.
51 is GENERATED.
52 is the same as 13 ROTATE_90.
53 is GENERATED.
54 is GENERATED.
55 is GENERATED.
56 is the same as 14 ROTATE_90.
57 is GENERATED.
58 is the same as 62 IDENTICAL.
59 is the same as 63 IDENTICAL.
60 is the same as 15 ROTATE_90.
61 is GENERATED.
62 is GENERATED.
63 is GENERATED.
64 is the same as 1 ROTATE_270.
65 is the same as 5 ROTATE_270.
66 is the same as 9 ROTATE_270.
67 is the same as 13 ROTATE_270.
68 is the same as 17 ROTATE_90.
69 is the same as 21 ROTATE_270.
70 is the same as 19 MIRROR.
71 is the same as 23 MIRROR.
72 is the same as 9 MIRROR_180.
73 is the same as 37 ROTATE_270.
74 is the same as 57 ROTATE_270.
75 is the same as 61 ROTATE_270.
76 is the same as 19 ROTATE_90.
77 is the same as 53 ROTATE_270.
78 is the same as 57 ROTATE_270.
79 is the same as 61 ROTATE_270.
80 is the same as 5 ROTATE_180.
81 is the same as 21 ROTATE_180.
82 is the same as 37 ROTATE_180.
83 is the same as 53 ROTATE_180.
84 is the same as 21 ROTATE_90.
85 is GENERATED.
86 is the same as 53 MIRROR_180.
87 is GENERATED.
88 is the same as 13 MIRROR_180.
89 is the same as 53 MIRROR_270.
90 is the same as 61 MIRROR_180.
91 is the same as 95 IDENTICAL.
92 is the same as 23 ROTATE_90.
93 is the same as 87 ROTATE_90.
94 is the same as 61 MIRROR_180.
95 is GENERATED.
96 is the same as 3 MIRROR_180.
97 is the same as 13 MIRROR_270.
98 is the same as 35 MIRROR_180.
99 is the same as 54 ROTATE_180.
100 is the same as 19 MIRROR_180.
101 is the same as 53 MIRROR.
102 is the same as 51 MIRROR.
103 is the same as 55 MIRROR.
104 is the same as 15 MIRROR_180.
105 is the same as 61 MIRROR_270.
106 is the same as 63 MIRROR_180.
107 is the same as 127 IDENTICAL.
108 is the same as 31 ROTATE_90.
109 is the same as 95 ROTATE_90.
110 is the same as 63 MIRROR_180.
111 is the same as 127 IDENTICAL.
112 is the same as 7 ROTATE_180.
113 is the same as 23 ROTATE_180.
114 is the same as 39 ROTATE_180.
115 is the same as 55 ROTATE_180.
116 is the same as 23 MIRROR_180.
117 is the same as 87 ROTATE_180.
118 is the same as 55 MIRROR_180.
119 is GENERATED.
120 is the same as 15 MIRROR_180.
121 is the same as 61 MIRROR_270.
122 is the same as 63 MIRROR_180.
123 is the same as 127 IDENTICAL.
124 is the same as 31 ROTATE_90.
125 is the same as 95 ROTATE_90.
126 is the same as 63 MIRROR_180.
127 is GENERATED.
128 is the same as 2 ROTATE_270.
129 is the same as 3 MIRROR_270.
130 is the same as 14 ROTATE_270.
131 is the same as 14 ROTATE_270.
132 is the same as 9 MIRROR.
133 is the same as 13 MIRROR.
134 is the same as 15 MIRROR.
135 is the same as 15 MIRROR.
136 is the same as 34 ROTATE_90.
137 is the same as 35 MIRROR_270.
138 is the same as 62 ROTATE_270.
139 is the same as 62 ROTATE_270.
140 is the same as 35 ROTATE_90.
141 is the same as 54 ROTATE_270.
142 is the same as 62 ROTATE_270.
143 is the same as 62 ROTATE_270.
144 is the same as 9 ROTATE_180.
145 is the same as 19 MIRROR_270.
146 is the same as 57 ROTATE_180.
147 is the same as 57 ROTATE_180.
148 is the same as 37 ROTATE_90.
149 is the same as 53 MIRROR_90.
150 is the same as 61 MIRROR_90.
151 is the same as 61 MIRROR_90.
152 is the same as 35 MIRROR_90.
153 is the same as 51 MIRROR_90.
154 is the same as 63 MIRROR_90.
155 is the same as 63 MIRROR_90.
156 is the same as 39 ROTATE_90.
157 is the same as 55 MIRROR_90.
158 is the same as 63 MIRROR_90.
159 is the same as 63 MIRROR_90.
160 is the same as 14 ROTATE_180.
161 is the same as 15 MIRROR_270.
162 is the same as 62 ROTATE_180.
163 is the same as 62 ROTATE_180.
164 is the same as 57 ROTATE_90.
165 is the same as 61 MIRROR.
166 is the same as 63 MIRROR.
167 is the same as 63 MIRROR.
168 is the same as 62 ROTATE_90.
169 is the same as 63 MIRROR_270.
170 is the same as 255 IDENTICAL.
171 is the same as 255 IDENTICAL.
172 is the same as 63 ROTATE_90.
173 is the same as 127 ROTATE_90.
174 is the same as 255 IDENTICAL.
175 is the same as 255 IDENTICAL.
176 is the same as 15 ROTATE_180.
177 is the same as 31 ROTATE_180.
178 is the same as 63 ROTATE_180.
179 is the same as 63 ROTATE_180.
180 is the same as 61 ROTATE_90.
181 is the same as 95 ROTATE_180.
182 is the same as 127 ROTATE_180.
183 is the same as 127 ROTATE_180.
184 is the same as 62 ROTATE_90.
185 is the same as 63 MIRROR_270.
186 is the same as 255 IDENTICAL.
187 is the same as 255 IDENTICAL.
188 is the same as 63 ROTATE_90.
189 is the same as 127 ROTATE_90.
190 is the same as 255 IDENTICAL.
191 is the same as 255 IDENTICAL.
192 is the same as 3 ROTATE_270.
193 is the same as 7 ROTATE_270.
194 is the same as 15 ROTATE_270.
195 is the same as 15 ROTATE_270.
196 is the same as 19 ROTATE_270.
197 is the same as 23 ROTATE_270.
198 is the same as 31 ROTATE_270.
199 is the same as 31 ROTATE_270.
200 is the same as 35 ROTATE_270.
201 is the same as 39 ROTATE_270.
202 is the same as 63 ROTATE_270.
203 is the same as 63 ROTATE_270.
204 is the same as 51 ROTATE_90.
205 is the same as 55 ROTATE_270.
206 is the same as 63 ROTATE_270.
207 is the same as 63 ROTATE_270.
208 is the same as 13 ROTATE_180.
209 is the same as 23 MIRROR_270.
210 is the same as 61 ROTATE_180.
211 is the same as 61 ROTATE_180.
212 is the same as 53 ROTATE_90.
213 is the same as 87 ROTATE_270.
214 is the same as 95 ROTATE_270.
215 is the same as 95 ROTATE_270.
216 is the same as 54 ROTATE_90.
217 is the same as 55 MIRROR_270.
218 is the same as 127 ROTATE_270.
219 is the same as 127 ROTATE_270.
220 is the same as 55 ROTATE_90.
221 is the same as 119 ROTATE_90.
222 is the same as 127 ROTATE_270.
223 is the same as 127 ROTATE_270.
224 is the same as 14 ROTATE_180.
225 is the same as 15 MIRROR_270.
226 is the same as 62 ROTATE_180.
227 is the same as 62 ROTATE_180.
228 is the same as 57 ROTATE_90.
229 is the same as 61 MIRROR.
230 is the same as 63 MIRROR.
231 is the same as 63 MIRROR.
232 is the same as 62 ROTATE_90.
233 is the same as 63 MIRROR_270.
234 is the same as 255 IDENTICAL.
235 is the same as 255 IDENTICAL.
236 is the same as 63 ROTATE_90.
237 is the same as 127 ROTATE_90.
238 is the same as 255 IDENTICAL.
239 is the same as 255 IDENTICAL.
240 is the same as 15 ROTATE_180.
241 is the same as 31 ROTATE_180.
242 is the same as 63 ROTATE_180.
243 is the same as 63 ROTATE_180.
244 is the same as 61 ROTATE_90.
245 is the same as 95 ROTATE_180.
246 is the same as 127 ROTATE_180.
247 is the same as 127 ROTATE_180.
248 is the same as 62 ROTATE_90.
249 is the same as 63 MIRROR_270.
250 is the same as 255 IDENTICAL.
251 is the same as 255 IDENTICAL.
252 is the same as 63 ROTATE_90.
253 is the same as 127 ROTATE_90.
254 is the same as 255 IDENTICAL.
255 is GENERATED.
```

## 4. License and copyright

This program is available under GPL v2. Please see the LICENSE and COPYRIGHT files for details.
