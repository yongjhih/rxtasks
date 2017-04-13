# RxTasks for GMS
[![CircleCI](https://circleci.com/gh/yongjhih/rxtasks.svg?style=shield)](https://circleci.com/gh/yongjhih/rxtasks)
[![codecov](https://codecov.io/gh/yongjhih/rxtasks/branch/master/graph/badge.svg)](https://codecov.io/gh/yongjhih/rxtasks)

## Usage

```java
RxTasks.completes(() -> firebaseRemoteConfig.fetch()).subscribe();
```

```java
RxTasks.single(() -> firebaseUser.getToken()).map(token -> token.getToken()).subscribe();
```

See [official documentation](https://firebase.google.com/docs/) for the details.

## Installation

```gradle
compile 'com.yongjhih.rxtasks:rxtasks2-tasks:0.0.1'
compile 'com.yongjhih.rxtasks:rxtasks2-tasks-kotlin:0.0.1' // optional
```

## License

```
Copyright 2017 Andrew Chen

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
