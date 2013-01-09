package org.codebin.faketileao;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {
	public final int[] BIT = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80 };
	public final int[] MASK = { 0xfe, 0xfd, 0xfb, 0xf7, 0xef, 0xdf, 0xbf, 0x7f };

	public int id = 0;
	public boolean processed = false;
	public Similarity similarity = Similarity.NONE;
	public int similarTo = 0;

	public Tile(int id) {
		this.id = id;
	}

	public void set(int similarTo, Similarity similarity) {
		this.similarTo = similarTo;
		this.similarity = similarity;
		processed = true;
	}

	public void rotate90() {
		for (int i = 0; i < 2; i++) {
			id <<= 1;
			if ((id & 0x100) == 0x100) id -= 0xff;
		}
	}

	public void swapBits(int a, int b) {
		int ba = (id & BIT[a]) == 0 ? 0 : BIT[b];
		int bb = (id & BIT[b]) == 0 ? 0 : BIT[a];
		id = (id & MASK[a] & MASK[b]) | ba | bb;
	}

	public void mirror() {
		swapBits(0, 2);
		swapBits(7, 3);
		swapBits(6, 4);
	}

	public int fillCorners() {
		int result = id;
		if ((result & BIT[7]) != 0 && (result & BIT[1]) != 0) result |= BIT[0];
		if ((result & BIT[1]) != 0 && (result & BIT[3]) != 0) result |= BIT[2];
		if ((result & BIT[3]) != 0 && (result & BIT[5]) != 0) result |= BIT[4];
		if ((result & BIT[5]) != 0 && (result & BIT[7]) != 0) result |= BIT[6];
		return result;
	}

	public BufferedImage getImage() {
		int size = Config.IMAGE_SIZE;

		BufferedImage image = new BufferedImage(size * 3, size * 3, BufferedImage.TYPE_BYTE_GRAY);
		Graphics2D g = image.createGraphics();
		g.setColor(Config.TILE_COLOR);
		g.fillRect(0, 0, size * 3, size * 3);

		g.setColor(Config.SHADOW_COLOR);

		if ((id & BIT[0]) != 0) g.fillRect(0, 0, size, size);
		if ((id & BIT[1]) != 0) g.fillRect(size, 0, size, size);
		if ((id & BIT[2]) != 0) g.fillRect(size * 2, 0, size, size);
		if ((id & BIT[3]) != 0) g.fillRect(size * 2, size, size, size);
		if ((id & BIT[4]) != 0) g.fillRect(size * 2, size * 2, size, size);
		if ((id & BIT[5]) != 0) g.fillRect(size, size * 2, size, size);
		if ((id & BIT[6]) != 0) g.fillRect(0, size * 2, size, size);
		if ((id & BIT[7]) != 0) g.fillRect(0, size, size, size);

		image = EffectUtils.gaussianBlur(image, null, Config.BLUR_RADIUS);

		return image.getSubimage(size, size, size, size);
	}
}
