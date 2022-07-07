class Sanity {
    public static boolean straightline_unsafe(int a, int b) {
	int x=a, y=b;
	if(a>0 && b>0) {
	    x = 2;
	} else {
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x; x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	}
	return false;
    }
    public static boolean straightline_safe(int a, int b) {
	int x=a, y=b;
	if(a>0 && b>0) {
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	} else {
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	    x=1+y; y=2+x; x=3+y; y=4+x; x=5+y; y=6+x; x=7+y; y=8+x; x=9+y; y=10+x;
	}
	return false;
    }
}
