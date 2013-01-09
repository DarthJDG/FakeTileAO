package org.codebin.faketileao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class Main {
	public static void main(String[] args) {
		// Create all possible tile layouts
		List<Tile> tiles = new ArrayList<Tile>();
		for (int i = 0; i < 256; i++) {
			tiles.add(new Tile(i));
		}

		// Check for similarities
		for (int i = 0; i < 256; i++) {
			Tile tile = tiles.get(i);

			// If already processed, skip
			if (tile.processed) continue;

			// A corner can be sealed by two sides, regardless if there is a wall
			// tile in the corner. If this layout has a hole in any of its corners,
			// set an IDENTICAL pointer to its filled in version, then skip.
			if (tile.fillCorners() != i) {
				tile.set(tile.fillCorners(), Similarity.IDENTICAL);
				continue;
			}

			// If we've got this far, this tile is processed and needs to be
			// generated.
			tile.processed = true;

			Tile trans = new Tile(i);

			// Search for and mark all mirrored and/or rotated versions of this
			// layout.

			trans.rotate90();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.ROTATE_90);
			}

			trans.rotate90();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.ROTATE_180);
			}

			trans.rotate90();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.ROTATE_270);
			}

			trans.id = i;

			trans.mirror();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.MIRROR);
			}

			trans.rotate90();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.MIRROR_90);
			}

			trans.rotate90();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.MIRROR_180);
			}

			trans.rotate90();
			if (trans.id != i && !tiles.get(trans.id).processed) {
				tiles.get(trans.id).set(i, Similarity.MIRROR_270);
			}
		}

		// Now that each layout is evaluated, resolve IDENTICAL pointers to actual
		// transformations. Only keep the ones that point to unmodified, generated
		// layouts. This makes all pointers directly resolvable.
		int sameAsResolved = 0;
		for (int i = 0; i < 256; i++) {
			Tile tile = tiles.get(i);
			if (tile.similarity == Similarity.IDENTICAL) {
				Tile resolved = tiles.get(tile.similarTo);
				if (resolved.similarTo != 0) {
					tile.set(resolved.similarTo, resolved.similarity);
					sameAsResolved++;
				}
			}
		}

		System.out.printf(sameAsResolved + " pointers resolved.\n");

		int toGenerate = 0;
		for (int i = 0; i < 256; i++) {
			if (tiles.get(i).similarTo == 0) toGenerate++;
		}

		for (int i = 0; i < 256; i++) {
			Tile tile = tiles.get(i);
			if (tile.similarTo == 0) {
				System.out.printf(i + " is GENERATED.\n");
			} else {
				System.out.printf(i + " is the same as " + tile.similarTo + " " + tile.similarity + ".\n");
			}
		}

		System.out.printf("Generating " + toGenerate + " lightmaps...\n\n");

		// Generating output PNGs
		new File("generated").mkdirs();
		for (int i = 0; i < 256; i++) {
			Tile tile = tiles.get(i);
			if (tile.similarTo == 0) {
				try {
					BufferedImage image = tile.getImage();
					File file = new File("generated/" + i + ".png");
					ImageIO.write(image, "png", file);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.printf("done.");
	}
}
