package co.btssstudio.checkergame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Schedule {
	public static int now = 0;
	static HashMap<Integer, Supplier<?>> list = new HashMap<Integer, Supplier<?>>();
	static HashMap<Integer, LongTimeEvent> LTEs = new HashMap<Integer, LongTimeEvent>();
	static HashSet<LongTimeEvent> activatedLTEs = new HashSet<LongTimeEvent>();
	public static void add(int starts, Supplier<?> func) {
		list.put(now + starts, func);
	}
	public static void addLTE(int starts, int lasts, Consumer<Integer> func) {
		LTEs.put(now + starts, new LongTimeEvent(lasts, func));
	}
	public static void addLTE(int starts, int lasts, Consumer<Integer> func, Supplier<?> finalize) {
		LTEs.put(now + starts, new LongTimeEvent(lasts, func, finalize));
	}
	public static void addSec(int startsecs, Supplier<?> func) {
		add(startsecs * Main.TICKRATE, func);
	}
	public static void update() {
		if(!list.isEmpty()) {
			Supplier<?> sup = list.get(now);
			if(sup != null) {
				sup.get();
				list.remove(now, sup);
			}
		} else if(!LTEs.isEmpty()){
			LongTimeEvent activatedLTE = LTEs.get(now);
			if(activatedLTE != null) {
				activatedLTE.activate();
				activatedLTEs.add(activatedLTE);
			}
		}
		for(LongTimeEvent lte : activatedLTEs) {
			lte.update();
		}
		now++;
	}
	static class LongTimeEvent {
		int past;
		int last;
		Consumer<Integer> func;
		Supplier<?> finalize;
		boolean activated = false;
		LongTimeEvent(int last, Consumer<Integer> func){
			this.last = last;
			this.func = func;
		}
		LongTimeEvent(int last, Consumer<Integer> func, Supplier<?> finalize){
			this.last = last;
			this.func = func;
			this.finalize = finalize;
		}
		public void activate() {
			past = now;
			activated = true;
		}
		public void update() {
			if(activated) {
				func.accept((Integer) (now - past));
				if(now - past == last) {
					activated = false;
					if(finalize != null) {
						finalize.get();
					}
				}
			}
		}
	}
}
