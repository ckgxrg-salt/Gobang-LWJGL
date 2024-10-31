package co.btssstudio.checkergame;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.util.LinkedList;

import static org.lwjgl.opengl.GL40.*;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;

public class Main {
	//变量区
	
	static int STATE = 0;
	
	static GLFWVidMode mode;
	static long window;
	static final int TICKRATE = 30;
	static final double NANOPERTICK = 1000000000.0d / TICKRATE;
	static long now;
	static long past;
	static long difference;
	
	static int vShaderObject;
	static int fShaderObject;
	static int program;
	
	static int bgcf = 0;
	static float bgcfr = 1f;
	static float bgcfg = 1f;
	static float bgcfb = 1f;
	
	static boolean leftPressed = false;
	static boolean leftClicked = false;
	
    static int i = 0;
    static int j = 0;
    static float[] offset = new float[3];
    
    static GameObject board;
    static LinkedList<GameObject> lobjh= new LinkedList<GameObject>();
    static LinkedList<GameObject> lobjv= new LinkedList<GameObject>();

    static Shader general;
    static Shader texed;
	
    static int width;
    static int height;
    static double whratio;
    static float lbx;
	static float lby;
	static float lty;
	static float bsw;
	static float bsh;
	static float bintervalw;
	static float bintervalh;
	
	static double mousex, mousey;
	
	static Piece[][] pieces = new Piece[20][20];
	static boolean isEmpty = true;
	static LinkedList<Piece> wonPieces = new LinkedList<Piece>();
	static BoardCoordinate currentCoordinate = new BoardCoordinate(0,0);
	static BoardCoordinate lastCoordinate = new BoardCoordinate(-1,-1);
	static Piece currentPiece;
	static LinkedList<GameObject> wonMarks = new LinkedList<GameObject>();
	static boolean blackTurn = true;
	static boolean blackFirst = true;
	static Button bl, br;
	static int blackwins = 0;
	static int whitewins = 0;
	
	static Texture circle;
	static Texture victory_mark;
	static Texture exit;
	static Texture reset;
	static Texture black;
	static Texture white;
	
	public static Label left_label;
	public static Label right_label;
    
	public static void main(String[] args) {
		glfwInit();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
	    glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
	    glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
	    glfwWindowHint(GLFW_FOCUSED, GLFW_TRUE);
	    //glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
	    //glfwWindowHint(GLFW_DECORATED, GLFW_FALSE);
	    mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
	    width = mode.width();
	    height = mode.height();
	    whratio = (double)height / (double)width;
	    bsw = (float) (0.98 * 2 * whratio) /90;
	    bsh = (float) (0.98 * 2) / 90;
	    bintervalw = (float) (0.98 * 2 * whratio) /20;
	    bintervalh = (float) (0.98 * 2) / 20;
	    lbx = (float) (-0.98 * whratio);
		lby = -0.98f;
		lty = 0.98f;
	    window = glfwCreateWindow(width, height, "CheckerGame", NULL, NULL);
	    glfwMakeContextCurrent(window);
	    GL.createCapabilities();
	    general = new Shader("assets/shaders/general.vert", "assets/shaders/general.frag");
	    texed = new Shader("assets/shaders/texed.vert", "assets/shaders/texed.frag");
	    circle = new Texture("assets/textures/circle.png");
	    victory_mark = new Texture("assets/textures/victory_mark.png");
	    exit = new Texture("assets/textures/exit.png");
	    reset = new Texture("assets/textures/reset.png");
	    black = new Texture("assets/textures/black.png");
	    white = new Texture("assets/textures/white.png");
	    left_label = new Label(new BoardCoordinate(-5, 13), black);
	    right_label = new Label(new BoardCoordinate(23, 13), white);
	    glEnable(GL_BLEND);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	    bl = new Button(new BoardCoordinate(-5, 9), () -> {
	    	return true;
	    });
	    br = new Button(new BoardCoordinate(23, 9), () -> {
	    	return true;
	    });
		byte[] boardindicies = {
				0,1,2,
				0,3,2
		};
		board = new GameObject(boardindicies, general, true);
		board.color(0.836f, 0.7f, 0.353f, 1f);
		byte[] index = {
				0, 1, 3, //左下，右下，右上
				0, 2, 3 //左下，左上，右上
		};
		for(int j=1;j<=19;j++) {
			lobjh.add(new GameObject(index, general, true));
			lobjv.add(new GameObject(index, general, true));
		}
		/*testline = new GameObject(new float[] {
				lbx, lby + stride, 0.2f,
				lbx + 0.5f, lby + stride, 0.2f,
				lbx, lby + stride + 0.1f, 0.2f,
				lbx + 0.5f, lby + stride + 0.1f, 0.2f
		}, index, true);*/
		//testline.color(1f, 1f, 1f, 1f);
	    glfwShowWindow(window);
	    glViewport(0, 0, width, height);
	    glfwSwapInterval(2);
	    glfwSetMouseButtonCallback(window, new ButtonCallBack());
	    glfwSetCursorPosCallback(window, new PosCallBack());
	    past = System.nanoTime();
	    while(!glfwWindowShouldClose(window)) {
	    //帧率控制
	    	glfwPollEvents();
	    	now = System.nanoTime();
	    	difference = now - past;
	    	if(difference >= NANOPERTICK) {
	    		difference -= NANOPERTICK;
	    		glClear(GL_COLOR_BUFFER_BIT);
	    		glClearColor(bgcfr,bgcfg,bgcfb,1.0f);
	    		processMousePos();
	    		Schedule.update();
	    		bl.update();
	    		br.update();
	    		//tick();
	    		if(STATE == 0)
	    			beginning();
	    		if(STATE == 1)
	    			waiting();
	    		if(STATE == 2)
	    			winning();
	    		}
	    		board.draw();
    			for(GameObject go : lobjh) {
    				go.draw();
    			}
    			for(GameObject go : lobjv) {
    				go.draw();
    			}
    			bl.draw();
    			br.draw();
    			left_label.draw();
    			right_label.draw();
    			if(bl.clicked && br.clicked) {
    				if(!isEmpty) {
    					reset();
    				} else {
    					glfwSetWindowShouldClose(window, true);
    				}
    				bl.clicked = false;
    				br.clicked = false;
    			}
    			if(currentPiece != null)
    				currentPiece.draw();
    			for(Piece[] ps : pieces) {
    				for(Piece p : ps)
    					if(p != null)
    						p.draw();
    			}
    			for(GameObject p : wonMarks) {
    				if(p != null)
    					p.draw();
    			}
				glfwSwapBuffers(window);
				leftClicked = false;
	    }
	    glfwTerminate();
	}
	public static void reset() {
		isEmpty = true;
		bl.update(exit);
		br.update(exit);
		STATE = 1;
		currentPiece = null;
		pieces = new Piece[20][20];
		blackTurn = blackFirst;
	}
	public static void processMousePos() {
		double nposx = 2 * mousex / width - 1;
		double nposy = -1 * (2 * mousey / height - 1);
		int boardx = (int) ((nposx - lbx) / bintervalw);
		int boardy = (int) ((nposy - lby) / bintervalh);
		if(leftClicked){
			currentCoordinate.x = boardx;
			currentCoordinate.y = boardy;
		}
	}
	public static void beginning() {
		bgcf++;
		if(bgcf == 250)
				bgcf = 0;
		if(bgcf < 125) {
			bgcfr -= 0.008;
			bgcfg -= 0.008;
			bgcfb -= 0.008;
		} else {
			bgcfr += 0.008;
			bgcfg += 0.008;
			bgcfb += 0.008;
		}
		if(leftClicked) {
			offset[0] = 0.95f - bgcfr;
			offset[1] = 0.92f - bgcfg;
			offset[2] = 0.85f - bgcfb;
			i = 0;
			Schedule.addLTE(0, 60, (tick) -> {
				bgcfr += offset[0]/60;
				bgcfg += offset[1]/60;
				bgcfb += offset[2]/60;
			}, () -> {
				board.visible(true);
				return true;
			});
			Schedule.addLTE(60, 60, (tick) -> {
				float[] boardvertices = {
						(float) (-0.98 * whratio),(float) ((0.985/60)*tick),0f,
						(float) (-0.98 * whratio),(float) (-1*(0.985/60)*tick),0f,
						(float) (0.98 * whratio),(float) (-1*(0.985/60)*tick),0f,
						(float) (0.98 * whratio),(float) ((0.985/60)*tick),0f
				};
				board.update(boardvertices);
			}, () -> {
				j = 0;
				for(GameObject go : lobjh) {
					go.color(0f,0f,0f,1f);
					go.visible(true);
				}
				for(GameObject go : lobjv) {
					go.color(0f,0f,0f,1f);
					go.visible(true);
				}
				return true;
			}); 
			Schedule.addLTE(120, 90, (tick) -> {
				for(GameObject go : lobjh) {
					j++;
					go.update(new float[] {
							lbx, lby + bintervalh*j, 0f,
							lbx + bsw * tick, lby + bintervalh*j, 0f,
							lbx, lby + bintervalh*j + 0.01f, 0f,
							lbx + bsw * tick, lby + bintervalh*j + 0.01f, 0f
					});
				}
				j=0;
				for(GameObject go : lobjv) {
					j++;
					go.update(new float[] {
							lbx + bintervalw * j, lty - bsh*tick, 0f,
							(float) (lbx + bintervalw * j + (0.01 * whratio)), lty - bsh*tick, 0f,
							lbx + bintervalw * j, lty , 0f,
							(float) (lbx + bintervalw * j + (0.01 * whratio)), lty , 0f
					});
				}
				j=0;
			}, () -> {
				STATE = 1;
				bl.visible(true);
			    br.visible(true);
			    left_label.color(0f, 0f, 0f, 1f);
			    left_label.visible(true);
				right_label.visible(true);
				return true;
			});
			STATE = -1;
		}
	}
	public static void waiting() {
		if(leftClicked && currentCoordinate.x <= 19 && currentCoordinate.y <= 19 && currentCoordinate.x >= 0 && currentCoordinate.y >= 0) {
			if(currentCoordinate.equals(lastCoordinate)) {
				if(blackTurn) {
					currentPiece.color(0f,0f,0f,1f);
				} else {
					currentPiece.color(1f,1f,1f,1f);
				}
				pieces[currentCoordinate.x][currentCoordinate.y] = currentPiece;
				isEmpty = false;
				bl.update(reset);
				br.update(reset);
				lastCoordinate = new BoardCoordinate(-1, -1);
				if(checkhori() || checkvert() || checklefttop() || checkrighttop()) {
    				wonPieces.add(currentPiece);
    				if(blackTurn) {
    					blackwins++;
    					if(blackwins<=6)
    						wonMarks.add(new VictoryMark(new BoardCoordinate(-8+blackwins, 5)));
    				} else {
    					whitewins++;
    					if(whitewins<=6)
    						wonMarks.add(new VictoryMark(new BoardCoordinate(20+whitewins, 5)));
    				}
    				blackFirst = !blackFirst;
    				blackTurn = !blackFirst;
    				STATE = 2;
				}
				blackTurn = !blackTurn;
				if(blackTurn) {
					left_label.color(0f, 0f, 0f, 1f);
					right_label.color(0f, 0f, 0f, 0.2f);
				} else {
					left_label.color(0f, 0f, 0f, 0.2f);
					right_label.color(0f, 0f, 0f, 1f);
				}
			} else if(pieces[currentCoordinate.x][currentCoordinate.y] == null) {
				lastCoordinate = currentCoordinate.copy();
				if(blackTurn) {
					currentPiece = new Piece.Builder().xy(currentCoordinate).black(true).build();
				} else {
					currentPiece = new Piece.Builder().xy(currentCoordinate).black(false).build();
				}
			}
		}
	}
	public static void winning() {
		if(i<10) {
			for(Piece p : wonPieces) {
				p.color(1f, 0f, 0f, 0f);
			}
			i++;
		} else {
			for(Piece p : wonPieces) {
				p.color(1f, 0f, 0f, 1f);
			}
			i++;
		}
		if(i >= 20) {
			i=0;
		}
	}
	public static boolean checkhori() {
		int connected = 1;
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.x+i <= 19 && pieces[currentCoordinate.x+i][currentCoordinate.y] != null && pieces[currentCoordinate.x+i][currentCoordinate.y].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x+i][currentCoordinate.y]);
			} else {
				break;
			}
		}
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.x-i >= 0 && pieces[currentCoordinate.x-i][currentCoordinate.y] != null && pieces[currentCoordinate.x-i][currentCoordinate.y].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x-i][currentCoordinate.y]);
			} else {
				break;
			}
		}
		if(connected < 5)
			wonPieces.clear();
		return connected >= 5;
	}
	public static boolean checkvert() {
		int connected = 1;
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.y+i <= 19 && pieces[currentCoordinate.x][currentCoordinate.y+i] != null && pieces[currentCoordinate.x][currentCoordinate.y+i].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x][currentCoordinate.y+i]);
			} else {
				break;
			}
		}
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.y-i >= 0 && pieces[currentCoordinate.x][currentCoordinate.y-i] != null && pieces[currentCoordinate.x][currentCoordinate.y-i].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x][currentCoordinate.y-i]);
			} else {
				break;
			}
		}
		if(connected < 5)
			wonPieces.clear();
		return connected >= 5;
	}
	public static boolean checklefttop() {
		int connected = 1;
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.x+i <= 19 && currentCoordinate.y-i >= 0 && pieces[currentCoordinate.x+i][currentCoordinate.y-i] != null && pieces[currentCoordinate.x+i][currentCoordinate.y-i].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x+i][currentCoordinate.y-i]);
			} else {
				break;
			}
		}
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.x-i >= 0 && currentCoordinate.y+i <= 19 && pieces[currentCoordinate.x-i][currentCoordinate.y+i] != null && pieces[currentCoordinate.x-i][currentCoordinate.y+i].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x-i][currentCoordinate.y+i]);
			} else {
				break;
			}
		}
		if(connected < 5)
			wonPieces.clear();
		return connected >= 5;
	}
	public static boolean checkrighttop() {
		int connected = 1;
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.x+i <= 19 && currentCoordinate.y+i <= 19 && pieces[currentCoordinate.x+i][currentCoordinate.y+i] != null && pieces[currentCoordinate.x+i][currentCoordinate.y+i].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x+i][currentCoordinate.y+i]);
			} else {
				break;
			}
		}
		for(int i=1;i<=4;i++) {
			if(currentCoordinate.x-i >= 0 && currentCoordinate.y-i >= 0 && pieces[currentCoordinate.x-i][currentCoordinate.y-i] != null && pieces[currentCoordinate.x-i][currentCoordinate.y-i].black == currentPiece.black) {
				connected++;
				wonPieces.add(pieces[currentCoordinate.x-i][currentCoordinate.y-i]);
			} else {
				break;
			}
		}
		if(connected < 5)
			wonPieces.clear();
		return connected >= 5;
	}
}
	class PosCallBack extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			Main.mousex = xpos;
			Main.mousey = ypos;
		}
	}
	class ButtonCallBack extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_PRESS) {
				Main.leftPressed = true;
			}
			if(button == GLFW_MOUSE_BUTTON_LEFT && action == GLFW_RELEASE && Main.leftPressed) {
				Main.leftPressed = false;
				Main.leftClicked = true;
			}
		}
	}
	class BoardCoordinate {
		public int x, y;
		public BoardCoordinate(int x, int y){
			this.x = x;
			this.y = y;
		}
		public BoardCoordinate copy() {
			return new BoardCoordinate(this.x, this.y);
		}
		public boolean equals(BoardCoordinate target) {
			return (this.x == target.x && this.y == target.y);
		}
		public float[] toNormalized() {
			float nposx = this.x * Main.bintervalw + Main.lbx;
			float nposy = this.y * Main.bintervalh + Main.lby;
			return new float[] {
					nposx, nposy
			};
		}
		public void fromNormalized(float nposx, float nposy) {
			this.x = (int) ((nposx - Main.lbx) / Main.bintervalw);
			this.y = (int) ((nposy - Main.lby) / Main.bintervalh);
		}
	}
