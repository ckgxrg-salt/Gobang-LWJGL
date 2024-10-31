package co.btssstudio.checkergame;

import java.util.function.Supplier;

public class Button extends TexturedGameObject {
	BoardCoordinate basepos;
	BoardCoordinate extendpos;
	Supplier<?> func;
	boolean clicked = false;
	public Button(BoardCoordinate pos, Supplier<?> func) {
		super(new float[] {
				(float)pos.toNormalized()[0], (float)pos.toNormalized()[1], 0f,
				(float)pos.toNormalized()[0], (float)(pos.toNormalized()[1] + Main.bintervalh*2), 0f,
				(float)(pos.toNormalized()[0] + Main.bintervalw*2), (float)(pos.toNormalized()[1] + Main.bintervalh*2), 0f,
				(float)(pos.toNormalized()[0] + Main.bintervalw*2), (float)pos.toNormalized()[1], 0f
		}, new byte[] {
				0, 1, 2,
				2, 3, 0
		}, Main.exit);
		basepos = pos;
		extendpos = new BoardCoordinate(pos.x + 2, pos.y + 2);
		this.func = func;
	}
	public void update() {
		if(clicked) {
			this.color(0.2f, 0.2f, 0.2f, 1.0f);
		} else {
			this.color(0.7f, 0.7f, 0.7f, 1.0f);
		}
		if(Main.leftClicked && Main.STATE >= 1) {
			if(Main.currentCoordinate.x >= basepos.x && Main.currentCoordinate.y >= basepos.y && Main.currentCoordinate.x <= extendpos.x && Main.currentCoordinate.y <= extendpos.y){
				func.get();
				clicked = !clicked;
			}
		}
	}
}
