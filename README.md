# Sugar

**Syntactic sugar for Java** - Write expressive, functional-style Java code with less boilerplate.

Sugar is a lightweight utility library that brings the elegance of functional programming to Java. It provides a collection of static helper methods that let you write cleaner, more readable code without the verbosity that Java is known for.

## Why Sugar?

Java's standard library is powerful but verbose. Sugar fixes that:

```java
// Without Sugar
List<String> names = users.stream()
    .filter(u -> u.getAge() > 18)
    .map(User::getName)
    .collect(Collectors.toList());
String first = names.isEmpty() ? null : names.get(0);

// With Sugar
List<String> names = map(filter(users, u -> u.getAge() > 18), User::getName);
String first = first(names);
```

## Installation

### Maven
```xml
<dependency>
    <groupId>com.akalea</groupId>
    <artifactId>sugar</artifactId>
    <version>0.0.15</version>
</dependency>
```

### Import
```java
import static com.akalea.sugar.Collections.*;
import static com.akalea.sugar.Strings.*;
import static com.akalea.sugar.Pojos.*;
import static com.akalea.sugar.Numbers.*;
import static com.akalea.sugar.Files.*;
import static com.akalea.sugar.Functions.*;
import static com.akalea.sugar.Parallel.*;
```

---

## Features at a Glance

| Module | Purpose |
|--------|---------|
| **Collections** | List/Set/Map creation, filtering, mapping, grouping, aggregations |
| **Strings** | String manipulation, case conversion, templates, parsing |
| **Pojos** | Null-safe operations, optional handling, reflection utilities |
| **Numbers** | Clamping, ranges, rounding, interpolation, safe parsing |
| **Files** | Simple file I/O, line reading, resource management |
| **Functions** | Composition, currying, memoization, partial application |
| **Parallel** | Parallel execution, retry logic, timeouts, debounce/throttle |
| **Try/Either/Validation** | Functional error handling without exceptions |

---

## Collections

Create and manipulate collections with minimal code.

### Creating Collections
```java
List<Integer> numbers = list(1, 2, 3, 4, 5);
Set<String> tags = set("java", "kotlin", "scala");
Map<String, Integer> scores = map(kv("alice", 95), kv("bob", 87));
```

### Transformations
```java
// Map and filter
List<String> upperNames = map(names, String::toUpperCase);
List<User> adults = filter(users, u -> u.getAge() >= 18);

// Flat map
List<String> allTags = flatMap(posts, Post::getTags);

// Distinct by property
List<User> uniqueByEmail = distinctBy(users, User::getEmail);
```

### Aggregations
```java
Integer total = sum(prices);
Double average = mean(scores);
Integer highest = max(values);
Map<String, List<Order>> byStatus = groupBy(orders, Order::getStatus);
Map<String, Integer> countByCategory = counts(products, Product::getCategory);
```

### Accessing Elements
```java
User first = first(users);                    // First element or null
User last = last(users);                      // Last element or null
User found = first(filter(users, u -> u.getId() == 42));  // Find by condition
List<User> top5 = take(users, 5);             // First N elements
List<User> rest = drop(users, 5);             // Skip first N elements
```

### Advanced Operations
```java
// Sliding windows
List<List<Integer>> windows = sliding(list(1,2,3,4,5), 3);
// [[1,2,3], [2,3,4], [3,4,5]]

// Running totals with scan
List<Integer> runningSum = scan(list(1,2,3,4), 0, (acc, x) -> acc + x);
// [0, 1, 3, 6, 10]

// Take/drop while condition holds
List<Integer> prefix = takeWhile(list(1,2,3,4,1), x -> x < 4);  // [1,2,3]
List<Integer> suffix = dropWhile(list(1,2,3,4,1), x -> x < 4);  // [4,1]

// Zip two lists together
List<Pair<String, Integer>> pairs = zip(names, ages);

// Interleave lists
List<Integer> mixed = interleave(list(1,3,5), list(2,4,6));  // [1,2,3,4,5,6]
```

### Predicate & Comparator Builders
```java
// Combine predicates
Predicate<User> filter = and(
    u -> u.getAge() >= 18,
    u -> u.isActive(),
    u -> u.getCountry().equals("US")
);

// Build comparators
Comparator<User> byAgeDesc = comparing(User::getAge, true);  // descending
List<User> sorted = sorted(users, byAgeDesc);
```

### Map Utilities
```java
Map<String, Integer> updated = mapValues(scores, v -> v + 10);
Map<String, String> keysMapped = mapKeys(data, String::toUpperCase);
Map<Integer, String> inverted = invert(map);  // Swap keys and values
Map<String, Integer> merged = merge(map1, map2, Integer::sum);
```

---

## Strings

Comprehensive string utilities without Apache Commons.

### Checks
```java
isEmpty(str)      // null or ""
isBlank(str)      // null, "", or whitespace only
isNotEmpty(str)   // opposite of isEmpty
isNotBlank(str)   // opposite of isBlank
```

### Manipulation
```java
truncate("Hello World", 8)          // "Hello..."
truncate("Hello World", 8, ">>")    // "Hello >"
padLeft("42", 5, '0')               // "00042"
padRight("Hi", 5, '.')              // "Hi..."
repeat("ab", 3)                     // "ababab"
reverse("hello")                    // "olleh"
```

### Case Conversion
```java
camelCase("hello_world")    // "helloWorld"
snakeCase("helloWorld")     // "hello_world"
kebabCase("helloWorld")     // "hello-world"
capitalize("hello")         // "Hello"
uncapitalize("Hello")       // "hello"
```

### Parsing & Extraction
```java
substringBefore("hello@world.com", "@")     // "hello"
substringAfter("hello@world.com", "@")      // "world.com"
substringAfterLast("a/b/c.txt", "/")        // "c.txt"
removePrefix("/api/users", "/api")          // "/users"
removeSuffix("file.txt", ".txt")            // "file"
split("a,b,c", ",")                         // ["a", "b", "c"]
```

### Templates
```java
String msg = template("Hello ${name}, you have ${count} messages",
    kv("name", "Alice"),
    kv("count", 5));
// "Hello Alice, you have 5 messages"
```

### Defaults
```java
defaultIfEmpty(str, "default")   // Returns "default" if str is null or ""
defaultIfBlank(str, "default")   // Returns "default" if str is null, "", or whitespace
nullToEmpty(str)                 // Returns "" if str is null
```

---

## Null-Safe Operations (Pojos)

Handle nulls gracefully without endless null checks.

### Safe Value Access
```java
// orElse - return default if null
String name = orElse(user.getName(), "Unknown");

// apply - transform if not null, else null
Integer length = apply(str, String::length);  // null if str is null

// ifPresent - execute only if not null
ifPresent(user, u -> sendEmail(u.getEmail()));

// coalesce - return first non-null value
String value = coalesce(primary, secondary, fallback);
```

### Safe Chaining
```java
// Chain through potentially null objects
String city = chain(user,
    u -> u.getAddress(),
    a -> a.getCity());  // null if any step is null

// safeGet - Optional-based safe access
Optional<String> city = safeGet(user,
    u -> u.getAddress(),
    a -> a.getCity());
```

### Conditional Execution
```java
// when - execute if condition is true
Optional<String> result = when(user.isActive(), () -> "Active User");

// unless - execute if condition is false
unless(list.isEmpty(), () -> process(list));
```

### Equality Checks
```java
equalsAny(status, "PENDING", "PROCESSING", "QUEUED")  // true if matches any
equalsNone(status, "FAILED", "CANCELLED")              // true if matches none
```

---

## Numbers

Numeric utilities for common operations.

### Clamping & Ranges
```java
int clamped = clamp(value, 0, 100);         // Keep value between 0-100
boolean valid = inRange(score, 0, 100);      // Check if in range
List<Integer> nums = range(0, 10, 2);        // [0, 2, 4, 6, 8]
```

### Rounding & Interpolation
```java
double rounded = roundTo(3.14159, 2);                    // 3.14
double interpolated = lerp(0, 100, 0.5);                 // 50.0
double mapped = mapRange(50, 0, 100, 0, 1);              // 0.5
```

### Safe Parsing
```java
int port = parseInt(portStr, 8080);           // Returns 8080 if parsing fails
double rate = parseDouble(rateStr, 0.0);      // Returns 0.0 if parsing fails
```

### Predicates
```java
isEven(4)  // true
isOdd(4)   // false
sign(-5)   // -1
```

---

## Files

Simple file I/O without boilerplate.

### Reading
```java
String content = read("/path/to/file.txt");
List<String> lines = lines("/path/to/file.txt");
```

### Writing
```java
write("/path/to/file.txt", "Hello, World!");
writeLines("/path/to/file.txt", list("line1", "line2"));
append("/path/to/file.txt", "More content");
```

### Resource Management
```java
// Auto-closing resources
String firstLine = using(
    () -> Files.newBufferedReader(path),
    reader -> reader.readLine()
);

// Process lines as stream
long count = withLines("/path/to/file.txt",
    stream -> stream.filter(line -> line.contains("ERROR")).count());
```

### Utilities
```java
exists("/path/to/file")
isFile("/path/to/file")
isDirectory("/path/to/dir")
listFiles("/path/to/dir")
mkdirs("/path/to/new/dir")
delete("/path/to/file")
```

---

## Functions

Function composition and manipulation utilities.

### Composition
```java
Function<String, Integer> length = String::length;
Function<Integer, Boolean> isEven = n -> n % 2 == 0;

// Compose: length then isEven
Function<String, Boolean> hasEvenLength = compose(length, isEven);
hasEvenLength.apply("hello");  // false (5 is odd)
```

### Partial Application & Currying
```java
BiFunction<Integer, Integer, Integer> add = (a, b) -> a + b;

// Partial application - fix first argument
Function<Integer, Integer> add5 = partial(add, 5);
add5.apply(3);  // 8

// Currying - transform to nested functions
Function<Integer, Function<Integer, Integer>> curriedAdd = curry(add);
curriedAdd.apply(5).apply(3);  // 8
```

### Memoization
```java
// Cache expensive computations
Function<Integer, Integer> fibonacci = memoize(n -> {
    if (n <= 1) return n;
    return fibonacci.apply(n-1) + fibonacci.apply(n-2);
});

// Lazy singleton
Supplier<Config> config = memoize(() -> loadConfigFromFile());
```

### Checked Exception Handling
```java
// Convert checked exceptions to unchecked
Function<String, byte[]> readBytes = unchecked(path -> Files.readAllBytes(Paths.get(path)));
```

---

## Parallel Execution

Concurrent operations made simple.

### Parallel Map & ForEach
```java
// Process items in parallel
List<Result> results = pMap(items, item -> expensiveOperation(item));

// Parallel forEach
pEach(users, user -> sendNotification(user));
```

### Retry Logic
```java
// Retry with fixed delay
String result = retry(
    () -> fetchFromApi(),
    3,                          // max attempts
    Duration.ofSeconds(1)       // delay between attempts
);

// Retry with exponential backoff
String result = retryWithBackoff(
    () -> fetchFromApi(),
    5,                          // max attempts
    Duration.ofMillis(100),     // initial delay
    2.0                         // multiplier
);
```

### Timeouts
```java
// Execute with timeout
Optional<String> result = timeout(
    () -> slowOperation(),
    Duration.ofSeconds(5)
);

// With default value
String result = timeout(
    () -> slowOperation(),
    Duration.ofSeconds(5),
    "default"
);
```

### Racing & Awaiting
```java
// Race - return first completed result
String fastest = race(
    () -> fetchFromServer1(),
    () -> fetchFromServer2(),
    () -> fetchFromServer3()
);

// Await all - wait for all to complete
List<String> allResults = awaitAll(
    () -> fetchFromServer1(),
    () -> fetchFromServer2()
);
```

### Debounce & Throttle
```java
// Debounce - only execute after quiet period
Runnable saveDebounced = debounce(
    () -> saveToDatabase(),
    Duration.ofMillis(500)
);

// Throttle - limit execution rate
Runnable logThrottled = throttle(
    () -> logMetrics(),
    Duration.ofSeconds(1)
);
```

### Timing
```java
// Measure execution time
Timed<Result> timed = timed(() -> expensiveOperation());
System.out.println("Took " + timed.getMillis() + "ms");
Result result = timed.getValue();
```

---

## Functional Error Handling

Handle errors without exceptions using monadic types.

### Try - Computation That May Fail
```java
import com.akalea.sugar.internal.Try;

// Wrap potentially failing code
Try<Integer> result = Try.of(() -> Integer.parseInt(userInput));

// Handle success/failure
result
    .onSuccess(n -> System.out.println("Parsed: " + n))
    .onFailure(e -> System.out.println("Invalid: " + e.getMessage()));

// Transform values
Try<String> doubled = result.map(n -> n * 2).map(Object::toString);

// Recover from failure
Integer value = result
    .recover(e -> 0)           // Default value on failure
    .get();

// Chain operations
Try<User> user = Try.of(() -> findUser(id))
    .flatMap(u -> Try.of(() -> validateUser(u)))
    .flatMap(u -> Try.of(() -> enrichUser(u)));
```

### Either - Union Type for Error Handling
```java
import com.akalea.sugar.internal.Either;

// Left = error, Right = success (by convention)
Either<String, User> result = validateUser(input);

// Handle both cases
String message = result.fold(
    error -> "Error: " + error,
    user -> "Welcome, " + user.getName()
);

// Transform the success value
Either<String, String> name = result.map(User::getName);

// Get with default
User user = result.getOrElse(defaultUser);
```

### Validation - Accumulate Multiple Errors
```java
import com.akalea.sugar.internal.Validation;

// Validate multiple fields, collecting all errors
Validation<String, String> nameV = Validation.valid(name)
    .filter(n -> !n.isEmpty(), "Name required");

Validation<String, Integer> ageV = Validation.valid(age)
    .filter(a -> a >= 0, "Age must be positive")
    .filter(a -> a < 150, "Age must be realistic");

// Combine validations
Validation<String, User> userV = nameV.combine(ageV, User::new);

// Get all errors if invalid
if (userV.isInvalid()) {
    List<String> errors = userV.getErrors();  // All validation errors
}
```

---

## Tuples

Lightweight tuple types for grouping values.

```java
// Pair - two values
Pair<String, Integer> pair = pair("Alice", 30);
String name = pair.getFirst();
Integer age = pair.getSecond();

// Tuple3 - three values
Tuple3<String, Integer, Boolean> t3 = tuple("Alice", 30, true);

// Tuple4 - four values
Tuple4<String, Integer, Boolean, String> t4 = tuple("Alice", 30, true, "US");

// Map operations
Pair<String, Integer> mapped = pair.mapFirst(String::toUpperCase);
```

---

## Real-World Examples

### Processing a CSV File
```java
List<User> users = map(
    drop(lines("/data/users.csv"), 1),  // Skip header
    line -> {
        List<String> parts = split(line, ",");
        return new User(
            parts.get(0),
            parseInt(parts.get(1), 0),
            parts.get(2)
        );
    }
);

Map<String, List<User>> byCountry = groupBy(users, User::getCountry);
```

### API Request with Retry and Timeout
```java
Optional<Response> response = timeout(
    () -> retry(
        () -> httpClient.get(url),
        3,
        Duration.ofSeconds(1)
    ),
    Duration.ofSeconds(10)
);

String body = response
    .map(Response::getBody)
    .orElse("Request failed");
```

### Data Transformation Pipeline
```java
List<Report> reports = map(
    filter(
        distinctBy(orders, Order::getCustomerId),
        o -> o.getTotal() > 100
    ),
    o -> new Report(
        o.getCustomerId(),
        orElse(o.getCustomerName(), "Unknown"),
        roundTo(o.getTotal(), 2)
    )
);
```

### Batch Processing with Progress
```java
List<List<Item>> batches = partition(items, 100);
pEach(batches, batch -> {
    retry(() -> processBatch(batch), 3, Duration.ofSeconds(5));
});
```

---

## Requirements

- Java 9+
- No external dependencies

## License

Apache License 2.0

## Contributing

Contributions are welcome! Please feel free to submit issues and pull requests.
