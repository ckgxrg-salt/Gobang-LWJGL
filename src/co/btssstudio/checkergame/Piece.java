package co.btssstudio.checkergame;

public class Piece extends TexturedGameObject{
	BoardCoordinate xy;
	public boolean black;
	Piece(BoardCoordinate xy, boolean black, float[] vert, byte[] index, Texture tex){
		super(vert, index, tex);
		this.xy = xy;
		this.black = black;
		if(black) {
			this.color(0f, 0f, 0f, 0.5f);
		} else {
			this.color(1f, 1f, 1f, 0.5f);
		}
		this.visible(true);
	}
	static class Builder {
		BoardCoordinate xy = new BoardCoordinate(0,0);
		boolean black;
		public Builder x(int x) {
			this.xy.x = x;
			return this;
		}
		public Builder y(int y) {
			this.xy.y = y;
			return this;
		}
		public Builder xy(BoardCoordinate xy) {
			this.xy = xy;
			return this;
		}
		public Builder black(boolean value) {
			this.black = value;
			return this;
		}
		public Piece build() {
			float nposx = xy.toNormalized()[0]+0.005f;
			float nposy = xy.toNormalized()[1];
			float[] vert = {
					nposx, nposy+0.01f, 0f,//LeftBottom
					nposx, nposy+Main.bintervalh, 0f,//LeftUpper
					nposx+Main.bintervalw-0.005f, nposy+Main.bintervalh, 0f,//RightUpper
					nposx+Main.bintervalw-0.005f, nposy+0.01f, 0f,//RightBottom
			};
			byte[] index = {
					0,1,2,
					2,3,0
			};
			return new Piece(xy, black, vert, index, Main.circle);
		}
	}
}
