package com.findit.model.interfaces;

/**
 * Interface for anything that can be searched by keyword.
 * OOP: Abstraction via interfaces.
 */
public interface Searchable {
    boolean matchesKeyword(String keyword);
}