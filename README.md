# IPD Android — 전시용 소스

지도 기반 장소 탐색, 리뷰·커뮤니티·쿠폰, QR 및 장치 제어 화면의 구조를 소개하는 Kotlin/Jetpack Compose 포트폴리오입니다. 소스 열람을 위한 저장소이며 빌드·실행·배포용 배포판이 아닙니다.

## 코드 둘러보기

기본 경로는 `app/src/main/java/com/example/ipdapplication`입니다.

| 경로 | 구현 내용 |
| --- | --- |
| `MainActivity.kt`, `MainScreen.kt` | 앱 진입과 화면 연결 |
| `NetworModule.kt` | Retrofit·OkHttp와 의존성 주입. 파일의 클래스명은 `NetworkModule`입니다. |
| `auth` | 로그인·가입 화면, 요청 DTO, 인증 헤더 처리 |
| `map`, `admin` | 장소·메뉴·리뷰·모빌리티 지도와 관리 화면 |
| `community`, `cupon` | 커뮤니티·쿠폰 화면과 데이터 처리 |
| `qr`, `led` | QR 스캔, 배터리 교체 단계와 LED 제어 화면 |
| `ui/theme`, `app/src/main/res` | 테마·벡터·기본 리소스 |

화면 → ViewModel → Repository → API 순서로 데이터 흐름을 읽을 수 있습니다. 기존 패키지명과 파일명은 대체로 보존했습니다.

## 전시용으로 정리한 부분

- API 기본 주소는 `https://example.invalid/`이고 지도 SDK ID는 실제 값이 아닌 placeholder입니다. `.env.example`은 설정 이름만 소개하며 실제 `.env`는 포함하지 않습니다.
- Gradle은 `.env.example`을 자동으로 읽지 않습니다. `app/build.gradle`은 `IPD_API_BASE_URL`, `NAVER_MAP_CLIENT_ID` Gradle property를 참조하고, 예시 파일에는 대응하는 `ORG_GRADLE_PROJECT_` 환경변수 이름을 적었습니다.
- 토큰은 전시용 메모리 세션에만 저장하도록 바꿨습니다. 디스크에 저장하지 않으며 프로세스가 종료되면 사라집니다. 401 응답 시 삭제하는 흐름은 유지했습니다.
- 좌표·주소·요청 데이터·장치 식별자 로그와 stack trace 출력을 제거하고 화면의 상세 예외는 일반 오류 문구로 바꿨습니다.
- 회원가입 화면에서 역할 선택을 제거했습니다. 일반 사용자로만 가입하며 관리 화면의 존재가 서버 권한을 의미하지 않습니다.
- 평문 HTTP와 앱 백업을 비활성화했습니다. 기존 인증 저장소의 백업 제외 규칙도 보존했습니다.
- 미리보기의 전화번호·주소·좌표·이메일·장치 ID는 전시용 예시로 바꾸고 외부 샘플 이미지 주소는 `example.invalid`로 치환했습니다.
- 중복 라이브러리·Compose BOM 선언과 사용하지 않는 HTTP 로깅 의존성을 정리했습니다. 라이브러리 버전 전반을 업그레이드하지는 않았습니다.
- 이전 Git 기록, IDE/AI 편집기 설정, 실제 설정 파일, 서명 키, APK/AAB, 로그와 업로드 파일은 포함하지 않습니다.

## 구현의 한계

화면·데이터 흐름을 보여주기 위해 서버 연동 코드와 관리 화면을 남겼습니다. 완성된 오프라인 데모 앱은 아니며 지도·이미지·API 호출을 실제로 사용할 수 없습니다. 앱을 실행하면 SDK가 자체 통신을 시도할 수 있으므로 네트워크가 전혀 없는 실행 환경을 제공한다는 뜻도 아닙니다.

QR 파싱은 원래 예제를 보존했으며 실제 적용에는 형식·길이 검사, 장치별 권한과 재전송 방지가 필요합니다. 운영 앱에는 서버 측 권한 검증, 안전한 세션 수명 관리, 카메라·위치 권한과 생명주기 검증도 필요합니다. Gradle property나 환경변수로 값을 옮겨도 빌드한 앱에 넣은 값이 비밀로 보호되는 것은 아닙니다. 서버 전용 secret을 클라이언트에 넣지 마세요.

기존 SDK·라이브러리 선언과 테스트 코드는 참고용입니다. 이 전시본은 빌드·테스트를 실행하지 않았으며 SDK 호환성, 최신 취약점 점검이나 실제 기기 동작을 보장하지 않습니다. 실제 서버·계정·장치와 연결하지 마세요.

## 소스 공개 항목 검사

`ruby scripts/check-public-source.rb`는 공개 금지 파일, 환경변수 예시, 민감 로그와 핵심 전시 설정을 검사합니다. 빌드·서비스 실행·외부 API 호출은 하지 않습니다. Git 과거 기록의 secret 검사는 이 스크립트와 별도로 수행해야 합니다.

## 저작권 및 외부 구성요소

기존 [LICENSE](LICENSE)의 권리 유보 조건을 유지합니다. 새로운 오픈소스 라이선스를 부여한 것은 아니며, 프로젝트 참여자의 공개 동의를 전제로 공개를 준비했습니다.

외부 라이브러리는 [app/build.gradle](app/build.gradle)과 [버전 목록](gradle/libs.versions.toml)에 명시되어 있고 각 구성요소의 라이선스·서비스 이용 조건을 따릅니다. 실제 사용자 사진, 서비스 캡처나 외부 샘플 이미지 파일은 추가하지 않았습니다.

Gradle wrapper(`gradle/wrapper/gradle-wrapper.jar`, `gradlew`, `gradlew.bat`)에는 [Apache License 2.0 전문](gradle/wrapper/LICENSE)이 적용되며 기존 파일의 저작권 고지를 유지합니다. 이 고지는 Gradle wrapper에 대한 것으로, 프로젝트 자체의 [LICENSE](LICENSE)를 변경하지 않습니다.
