# tiny-log

Tiny library for structured logging

```java
var event = Event.builder()
    .name("My Event")
    .category(Category.INFO)
    .put("counter", 1)
    .put("object", (it) -> it.put("nested", 2))
    .build();

StructuredLogs.AddTimestamp(clock)
    .then(StructuredLogs.AddServiceName("API#1"))
    .then(StructuredLogs.AddName())
    .then(StructuredLogs.AddCategory())
    .then(new PrintingEvents())
    .log(event);

// {"metadata":{"name":"My Event","category":"INFO","service":"API#1","timestamp":"1970-01-01T00:00:00Z"},"event":{"counter":1,"object":{"nested":2}}}
```
