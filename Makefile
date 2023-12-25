run-local-stack:
	echo "Running local stack .................."
	docker-compose -f docker-compose.yml -f docker-compose.kafka.yml up -d

clean-local-stack:
	echo "Shutting down local stack .................."
	docker-compose -f docker-compose.yml -f docker-compose.kafka.yml down
