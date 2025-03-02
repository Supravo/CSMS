description = "transaction-service"

dependencies {
	api(project(":csms-commons"))

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
}