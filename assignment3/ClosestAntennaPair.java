package starterCode;

import java.awt.*;
import java.util.Arrays;

public class ClosestAntennaPair {

    private double closestDistance = Double.POSITIVE_INFINITY;
    private long counter = 0;

    public ClosestAntennaPair(Point2D[] aPoints, Point2D[] bPoints) {
        // base case 1: one set has no points
        int n = aPoints.length;
        int m = bPoints.length;
        if (n == 0 || m == 0) return;

        Point2D[] aPointsSortedByX = new Point2D[n];
        for (int i = 0; i < n; i++)
            aPointsSortedByX[i] = aPoints[i];

        Point2D[] bPointsSortedByX = new Point2D[m];
        for (int i = 0; i < m; i++)
            bPointsSortedByX[i] = bPoints[i];

        // sort by X-coordinate
        Arrays.sort(aPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(aPointsSortedByX, Point2D.X_ORDER);
        Arrays.sort(bPointsSortedByX, Point2D.Y_ORDER);
        Arrays.sort(bPointsSortedByX, Point2D.X_ORDER);

        // if there are two co-incident points, then we are done because minimum distance is 0

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++ ){
                if (aPointsSortedByX[i].equals(bPointsSortedByX[j])){
                    closestDistance = 0.0;
                    return;
                }
            }
        }

        // set up the array that eventually will hold the points sorted by y-coordinate

        Point2D[] aPointsSortedByY = new Point2D[n];
        for (int i = 0; i < n; i++)
            aPointsSortedByY[i] = aPointsSortedByX[i];

        Point2D[] bPointsSortedByY = new Point2D[m];
        for (int i = 0; i < m; i++)
            bPointsSortedByY[i] = bPointsSortedByX[i];

        Point2D[] auxA = new Point2D[n];
        Point2D[] auxB = new Point2D[m];

        closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, 0, 0, n-1, m-1);
        }

    public double closest(Point2D[] aPointsSortedByX, Point2D[] bPointsSortedByX, Point2D[] aPointsSortedByY, Point2D[] bPointsSortedByY, Point2D[] auxA, Point2D[] auxB, int lowA, int lowB, int highA, int highB) {
        // please do not delete/modify the next line!
        counter++;

        // if combined set has 3 or fewer points
        if (highA + 1 + highB + 1 <= 3){
            for (int i = lowA; i <= highA; i++){
                for (int j = lowB; j <= highB; j++){
                    double distance = aPointsSortedByX[i].distanceTo(bPointsSortedByX[j]);
                    if (distance < closestDistance){
                        closestDistance = distance;
                    }
                }
            }
            return closestDistance;
        }

        // merging both sets sorted by X into pointsSortedByX
        int n = aPointsSortedByX.length;
        int m = bPointsSortedByX.length;
        Point2D[] pointsSortedByX = new Point2D[n+m];
        int aCounter = 0;
        int bCounter = 0;
        int index = 0;

        while ((aCounter < n) && (bCounter < m)){
            int comparison = aPointsSortedByX[aCounter].compareTo(bPointsSortedByX[bCounter]);
            if (comparison == -1){
                pointsSortedByX[index++] = aPointsSortedByX[aCounter++];
            }
            else if (comparison == 1){
                pointsSortedByX[index++] = bPointsSortedByX[bCounter++];
            }
        }
        while (aCounter < n){
            pointsSortedByX[index++] = aPointsSortedByX[aCounter++];
        }
        while (bCounter < m){
            pointsSortedByX[index++] = bPointsSortedByX[bCounter++];
        }

        // modifying lowA and lowB if starts at mid
        // sets lowA/lowB to the next point after mid that belongs to setA or setB
        // if there is none or low=high, return Double.POSITIVE_INFINITY
        if (lowA != 0 && lowB != 0){
            int counterA2 = 0;
            int counterB2 = 0;
            for (int i=lowA+1; i < pointsSortedByX.length; i++){
                if (belongsToSet(pointsSortedByX[i], aPointsSortedByX) && counterA2 == 0){
                    for (int j=0; j < aPointsSortedByX.length; j++){
                        if (aPointsSortedByX[j].equals(pointsSortedByX[i])){
                            lowA = j;
                            counterA2++;
                        }
                    }
                }
                if (belongsToSet(pointsSortedByX[i], bPointsSortedByX) && counterB2 == 0){
                    for (int j=0; j < bPointsSortedByX.length; j++){
                        if (bPointsSortedByX[j].equals(pointsSortedByX[i])){
                            lowB = j;
                            counterB2++;
                        }
                    }
                }
                if (counterA2 == 1 && counterB2 == 1){
                    break;
                }
            }
            if (counterA2 != 1){
                return Double.POSITIVE_INFINITY;
            }
            if (counterB2 != 1){
                return Double.POSITIVE_INFINITY;
            }
        }
        if (lowA == highA || lowB == highB){
            return Double.POSITIVE_INFINITY;
        }
        // calculate mid
        // (highA + highB + 1) represents the last index of the merged set
        int mid = Math.min(lowA,lowB) + ((highA+highB+1)-(Math.min(lowA,lowB))) / 2;

        // modifying highA and highB to be lower or equal to mid for next recursion
        // sets highA/highB to next point belonging to setA/setB that is less than or equal to mid
        // if none, return Double.POSITIVE_INFINITY
        int counterA = 0;
        int counterB = 0;
        for (int i=mid; i >= 0; i--){
            if (counterA == 1 && counterB == 1){
                break;
            }
            if (belongsToSet(pointsSortedByX[i], aPointsSortedByX) && counterA == 0){
                for (int j=0; j < aPointsSortedByX.length; j++){
                    if (aPointsSortedByX[j].equals(pointsSortedByX[i])){
                        highA = j;
                        counterA++;
                    }
                }
            }
            if (belongsToSet(pointsSortedByX[i], bPointsSortedByX) && counterB == 0){
                for (int j=0; j < bPointsSortedByX.length; j++){
                    if (bPointsSortedByX[j].equals(pointsSortedByX[i])){
                        highB = j;
                        counterB++;
                    }
                }
            }
        }
        if (counter > 1){
            if (counterA != 1){
                return Double.POSITIVE_INFINITY;
            }
            if (counterB != 1){
                return Double.POSITIVE_INFINITY;
            }
        }

        Point2D median = pointsSortedByX[mid];

        double delta1 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, lowA, lowB, highA, highB);
        double delta2 = closest(aPointsSortedByX, bPointsSortedByX, aPointsSortedByY, bPointsSortedByY, auxA, auxB, mid, mid, n-1, m-1);
        double delta = Math.min(delta1, delta2);

        // combining both sets sorted by Y into one
        Point2D[] pointsSortedByY = new Point2D[n+m];
        Point2D[] temp1 = new Point2D[mid + 1];
        for (int i = 0; i <= mid; i++){
            temp1[i] = pointsSortedByX[i];
        }
        Arrays.sort(temp1, Point2D.Y_ORDER);     // sorting [low ... mid] by Y

        Point2D[] temp2 = new Point2D[pointsSortedByX.length - (mid+1)];
        int r = 0;
        for (int i = mid+1; i < pointsSortedByX.length; i++){ // sorting [mid+1...high] by Y
            temp2[r] = pointsSortedByX[i];
            r++;
        }
        Arrays.sort(temp2, Point2D.Y_ORDER);

        for (int i = 0; i < temp1.length; i++){
            pointsSortedByY[i] = temp1[i];
        }
        for (int i = 0; i < temp2.length; i++){
            pointsSortedByY[mid+1+i] = temp2[i];   // combining both sets
        }

        Point2D[] aux = new Point2D[pointsSortedByX.length];
        merge(pointsSortedByY, aux, 0, mid, highA+highB+1);

        // aux[0..m-1] = go through the [low,high] range of pointsSortedByY and make a list of those points
        // whose x value is within delta from the median of x;  keep them sorted by y-coordinate
        // These are the points in a 2-delta strip around the x median for [low,high].
        // Note this wipes out any values in aux that were previously there, which is fine since aux is temporary only.

        int p = 0;
        for (int i = 0; i < pointsSortedByY.length; i++) {
                if (Math.abs(pointsSortedByY[i].x() - median.x()) < delta)
                        aux[p++] = pointsSortedByY[i];
        }

        // Compare pairs of points within the strip;  we only need to test points with a y separation less than delta
        // Find the closest pair of points from distinct sets and return the distance between them

        for (int i = 0; i < p; i++) {
            for (int j = i+1; (j < p) && (aux[j].y() - aux[i].y() < delta); j++) {
                if ((belongsToSet(aux[j], aPointsSortedByY) && belongsToSet(aux[i], bPointsSortedByY)) ||
                        (belongsToSet(aux[j], bPointsSortedByY) && belongsToSet(aux[i], aPointsSortedByY)))
                {
                    double distance = aux[i].distanceTo(aux[j]);
                    if (distance < delta) {
                        delta = distance;
                        if (distance < closestDistance)
                            closestDistance = delta;      // update closestDistance
                    }
                }
            }
        }
        return delta;
    }

    //helper function to check if point belongs to setA
    public boolean belongsToSet(Point2D point, Point2D[] setA) {
        for (int i = 0; i < setA.length; i++){
                if (setA[i].equals(point)) {
                    return true;
                }
        }
        return false;
    }

    public double distance() {
        return closestDistance;
    }

    public long getCounter() {
        return counter;
    }

    // stably merge a[low .. mid] with a[mid+1 ..high] using aux[low .. high]
    // precondition: a[low .. mid] and a[mid+1 .. high] are sorted sub-arrays, namely sorted by y coordinate
    // this is the same as in ClosestPair
    private static void merge(Point2D[] a, Point2D[] aux, int low, int mid, int high) {
        // copy to aux[]
        // note this wipes out any values that were previously in aux in the [low,high] range we're currently using

        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) a[k] = aux[j++];   // already finished with the low list ?  then dump the rest of high list
            else if (j > high) a[k] = aux[i++];   // already finished with the high list ?  then dump the rest of low list
            else if (aux[i].compareByY(aux[j]) < 0)
                a[k] = aux[i++]; // aux[i] should be in front of aux[j] ? position and increment the pointer
            else a[k] = aux[j++];
        }
    }
}
