class Sanity {
    public static boolean sanity_safe(int a, int b) {
        int i = b;
        int j = b;
        if (b<0) return false;

        if (a>0) {
            while (i > 0) {
                i--;
            }
        } else {
            while (j > 0) {
                j--;
            }

        }
        return false;
    }

    public static boolean sanity_unsafe(int a, int b) {
        int i = b;
        int j = b;
        if (b<0) return false;

        if (a<0) {
            return true;
        } else {
            while (i > 0) {
                i--;
            }
        }
        return false;
    }
}
