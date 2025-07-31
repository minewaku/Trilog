package com.minewaku.trilog.search.repository.custom;

import java.util.Collections;
import java.util.List;

import com.minewaku.trilog.dto.common.response.CursorPage;
import com.minewaku.trilog.dto.model.Cursor;

public interface CursorPageBuilder {
	static CursorPage<Integer> buildCursorResponse(List<Integer> list, Cursor cursor) {
        int limit = cursor.getLimit() <= 0 ? 10 : cursor.getLimit();

        if (list.isEmpty()) {
            return CursorPage.<Integer>builder()
                    .after(null)
                    .before(null)
                    .limit(limit)
                    .total(0)
                    .records(Collections.emptyList())
                    .build();
        }

        Integer nextCursor = null;
        Integer prevCursor = null;
        List<Integer> records = list;

        if (cursor.getAfter() == null && cursor.getBefore() == null) {
            if (list.size() > limit) {
                nextCursor = list.get(limit - 1);
                records = list.subList(0, limit);
            }
        } else if (cursor.getAfter() != null && cursor.getBefore() == null) {
            if (list.size() > limit) {
                nextCursor = list.get(limit - 1);
                records = list.subList(0, limit);
                prevCursor = cursor.getAfter();
            } else {
                prevCursor = cursor.getAfter();
            }
        } else if (cursor.getBefore() != null && cursor.getAfter() == null) {
            if (list.size() > limit) {
                prevCursor = list.get(0);
                records = list.subList(0, limit);
                nextCursor = cursor.getBefore();
            } else {
                nextCursor = cursor.getBefore();
            }
        } else {
            if (list.size() > limit) {
                nextCursor = list.get(limit - 1);
                records = list.subList(0, limit);
                prevCursor = cursor.getAfter();
            } else {
                prevCursor = cursor.getAfter();
            }
        }

        return CursorPage.<Integer>builder()
                .after(nextCursor)
                .before(prevCursor)
                .limit(limit)
                .total(list.size())
                .records(records)
                .build();
    }
}
