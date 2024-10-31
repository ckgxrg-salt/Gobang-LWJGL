package co.btssstudio.checkergame;

public class VictoryMark extends TexturedGameObject {
	public VictoryMark(BoardCoordinate pos) {
		super(new float[] {
				(float)pos.toNormalized()[0], (float)pos.toNormalized()[1], 0f,
				(float)pos.toNormalized()[0], (float)(pos.toNormalized()[1] + Main.bintervalh), 0f,
				(float)(pos.toNormalized()[0] + Main.bintervalw), (float)(pos.toNormalized()[1] + Main.bintervalh), 0f,
				(float)(pos.toNormalized()[0] + Main.bintervalw), (float)pos.toNormalized()[1], 0f
		}, new byte[] {
				0, 1, 2,
				2, 3, 0
		}, Main.victory_mark);
		this.color(1f, 1f, 1f, 1f);
		this.visible(true);
	}
}
