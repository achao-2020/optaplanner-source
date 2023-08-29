package org.optaplanner.examples.nqueens.domain.solver;

import java.util.Comparator;

import org.optaplanner.core.impl.heuristic.selector.common.decorator.SelectionSorterWeightFactory;
import org.optaplanner.examples.nqueens.domain.NQueens;
import org.optaplanner.examples.nqueens.domain.Row;

public class RowStrengthWeightFactory implements SelectionSorterWeightFactory<NQueens, Row> {

    private static int calculateDistanceFromMiddle(int n, int columnIndex) {
        int middle = n / 2;
        // 距离中间的距离
        int distanceFromMiddle = Math.abs(columnIndex - middle);
        if ((n % 2 == 0) && (columnIndex < middle)) {
            // 如果是偶数，且在中间的左边，距离中间的距离减一
            distanceFromMiddle--;
        }
        return distanceFromMiddle;
    }

    @Override
    public RowStrengthWeight createSorterWeight(NQueens nQueens, Row row) {
        int distanceFromMiddle = calculateDistanceFromMiddle(nQueens.getN(), row.getIndex());
        return new RowStrengthWeight(row, distanceFromMiddle);
    }

    public static class RowStrengthWeight implements Comparable<RowStrengthWeight> {

        // The stronger rows are on the side, so they have a higher distance to the middle
        private static final Comparator<RowStrengthWeight> COMPARATOR = Comparator
                .comparingInt((RowStrengthWeight weight) -> weight.distanceFromMiddle)
                .thenComparingInt(weight -> weight.row.getIndex());

        private final Row row;
        private final int distanceFromMiddle;

        public RowStrengthWeight(Row row, int distanceFromMiddle) {
            this.row = row;
            this.distanceFromMiddle = distanceFromMiddle;
        }

        @Override
        public int compareTo(RowStrengthWeight other) {
            return COMPARATOR.compare(this, other);
        }
    }

    public static void main(String[] args) {
        RowStrengthWeight weight1 = new RowStrengthWeight(new Row(2), 6);
        RowStrengthWeight weight2 = new RowStrengthWeight(new Row(6), 2);
        int compare = RowStrengthWeight.COMPARATOR.compare(weight1, weight2);
        System.out.println(compare);
    }
}
