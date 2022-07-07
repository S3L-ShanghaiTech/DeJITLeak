class MoreSanity {
    public static boolean array_safe(int a[], int taint) {
        int t;
        if (taint < 0) {
            int i = a.length-1;
            while (i >= 0) {
                t = a[i];
                i--;
            }
        } else {
            int i = 0;
            while (i < a.length) {
                t = a[i] / 2;
                i++;
            }
        }
        return false;
    }

    public static boolean array_unsafe(int a[], int taint) {
        System.out.println(a.length);
        int t;
        if (taint < 0) {
            int i = a.length-1;
            // int i = a.length;
            while (i >= 0) {
                t = a[i];
                i--;
            }
        } else {
            int i = 0;
            t = a[i] / 2;
            i = a.length;
        }
        return false;
    }
}
