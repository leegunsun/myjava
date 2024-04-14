# 첫 번째 단계: 빌드 환경 설정
FROM openjdk:17-jdk-slim as build
WORKDIR /workspace/app

# Gradle Wrapper 및 소스 코드 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./
COPY src src

# 파일 권한 설정 및 라인 엔딩 점검
RUN chmod +x ./gradlew
RUN sed -i 's/\r$//' ./gradlew    # Windows에서 복사된 파일의 라인 엔딩을 Unix 스타일로 변환

# 파일 정보 및 내용 출력을 통한 디버깅
#RUN ls -la                       # 파일 권한 및 세부 정보 확인
#RUN cat ./gradlew                # 스크립트 파일 내용 확인

# 프로젝트 빌드
RUN ./gradlew build -x test

# 두 번째 단계: 실행 환경 설정
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/build/libs
COPY --from=build ${DEPENDENCY}/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
