docker-compose up -d
# sleep 20s
while `docker exec --interactive coffeebear_dbserver_1 mysql --host localhost --default-character-set=utf8mb4 --user=root --password=PASSWORD coffee_bear`
do
    sleep 1s
done
docker exec --interactive coffeebear_dbserver_1 mysql --host localhost --default-character-set=utf8mb4 --user=root --password=PASSWORD coffee_bear < data.sql
