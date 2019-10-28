# snowflake
standart implementation twitter's [snowflake id generation](https://developer.twitter.com/en/docs/basics/twitter-ids)

to add dependency append to your build.gradle file:
```gradle
repositories {
    maven{
        url 'https://maven.pkg.github.com/teheidoma/snowflake'
    }
}
```
and then 
```gradle
implementation("com.teheidoma:snowflake:1.0")
```
