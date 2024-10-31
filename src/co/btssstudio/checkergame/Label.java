package co.btssstudio.checkergame;

public class Label extends TexturedGameObject {
	public Label(BoardCoordinate pos, Texture tex) {
		super(new float[] {
				(float)pos.toNormalized()[0], (float)pos.toNormalized()[1], 0f,
				(float)pos.toNormalized()[0], (float)(pos.toNormalized()[1] + Main.bintervalh*2), 0f,
				(float)(pos.toNormalized()[0] + Main.bintervalw*2), (float)(pos.toNormalized()[1] + Main.bintervalh*2), 0f,
				(float)(pos.toNormalized()[0] + Main.bintervalw*2), (float)pos.toNormalized()[1], 0f
		}, new byte[] {
				0, 1, 2,
				2, 3, 0
		}, tex);
		this.color(0f, 0f, 0f, 0.2f);
	}
}
