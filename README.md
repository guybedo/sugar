Sugar
===================

Syntactic sugar for Java


What is this?
--------------

Sugar is a small library providing some static imports to write less verbose Java code. 

Nothing groundbreaking here, just some sugar.

Features:
- Create/Manipulate collections: set, list, map, partition, product, intersect
- Collection helpers: first, last, sorted, max, min, mean, sum, prepend, append, concat, zip


# Examples

## Map
Before

```Java
Map<String, String> newMap = new HashMap<>();
newMap.put("key1", "value1");
newMap.put("key2", "value2");
newMap.put("key3", "value3");

```

After

```Java
Map<String, String> newMap = map(kv("key1", "value1"), kv("key1", "value1"), kv("key1", "value1"));

```

## List
Before

```Java
List<Integer> values = new ArrayList();
values.add(1);
values.add(2);
values.add(3);
values.add(4);
Integer min =
    values
        .stream()
        .sorted((a, b) -> -a.compareTo(b))
        .findFirst()
        .orElse(null);
Integer last = values.get(values.size()-1);

Integer one =
    values
        .stream()
        .filter(v -> v == 1)
        .findFirst()
        .orElse(null);
        
values =
    values
        .stream()
        .map(v -> v + 1)
        .collect(Collectors.toList());

```

After

```Java
List<Integer> values = list(1,2,3,4);
Integer min = min(values);
Integer last = last(values);
Integer one = first(filter(values,v -> v == 1));
values = map(values, v -> v + 1);

```

## POJOs
Before

```Java
Optional
	.ofNullable("test")
	.map(s -> s + "_ok")
	.orElse(null);
Optional
    .ofNullable("test")
    .ifPresent(s -> System.out.println(s));
LocalDateTime
    .now()
    .format(DateTimeFormatter.ISO_DATE_TIME);
```

After

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
    <version>0.0.2</version>
</dependency>
```

Import functions
----------------

```Java
import static com.akalea.sugar.Collections.*;
import static com.akalea.sugar.Pojos.*;
```
