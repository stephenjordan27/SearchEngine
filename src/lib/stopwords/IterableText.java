/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lib;

import java.util.Iterator;

abstract class IterableText implements Iterator<String>, Iterable<String> {
        public Iterator<String> iterator() {
                return this;
        }
}
