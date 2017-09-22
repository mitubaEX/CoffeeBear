docker-compose up -d
# sleep 20s
until mysqladmin ping -h 0.0.0.0 --silent;
do
    echo 'waiting for mysqld to be connectable...'
    sleep 1s
done
docker exec --interactive coffeebear_dbserver_1 mysql --host localhost --default-character-set=utf8mb4 --user=root --password=PASSWORD coffee_bear < data.sql
