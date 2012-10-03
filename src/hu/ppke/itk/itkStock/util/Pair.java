package hu.ppke.itk.itkStock.util;

public class Pair<T1, T2> {
	public final T1 fst;
	public final T2 snd;

	public Pair(T1 fst, T2 snd) {
		this.fst = fst;
		this.snd = snd;
	}

	@Override public String toString() {
		return "("+fst+":"+snd+")";
	}

	@Override public int hashCode() {
		if(fst == null) return ( snd == null? 0 : snd.hashCode()  );
		else if(snd == null) return fst.hashCode();
		return fst.hashCode()*17+snd.hashCode()*31;
	}

	@Override public boolean equals(Object other) {
		if(other instanceof Pair) {
			Pair<?,?> that = (Pair<?,?>)other;
			return ( fst.equals(that.fst) && snd.equals(that.snd) );
		}
		return false;
	}
}
