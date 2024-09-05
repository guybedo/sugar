Sugar
===================

Sugar for Java


What is this?
--------------

Sugar is a small utils library providing static functions to write less verbose Java code. 

Nothing groundbreaking here, just some sugar.

Features:
- Create/Manipulate collections: set, list, map, partition, product, intersect, counts, sums
- Collection helpers: first, last, sorted, max, min, mean, sum, prepend, append, concat, zip
- Random: random string, random char, choose elements in collection
- Parallel: pEach, pMap


# Examples

## Collections
```Java
Map<String, String> newMap = map(kv("key1", "value1"), kv("key1", "value1"), kv("key1", "value1"));

List<Integer> values = list(1,2,3,4);
Integer min = min(values);
Integer last = last(values);
Integer one = first(filter(values,v -> v == 1));
values = map(values, v -> v + 1);

Map<Profile, Integer> counts = counts(objects, o -> o.getProfile());

```

## POJOs
```Java
orElse("test", s -> s + "_ok", null);
ifPresent("test", s -> System.out.println(s));
isoDateTime(LocalDateTime.now());
```


Getting Started
---------------

Maven
-----

```xml
<dependency>
    <groupId>com.akalea</groupId>
    <artifactId>sugar</artifactId>
    <version>0.0.10</version>
</dependency>
```

Import functions
----------------

```Java
import static com.akalea.sugar.Collections.*;
import static com.akalea.sugar.Pojos.*;
```
