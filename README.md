# superPay
docker push 1350815230/test
docker pull 1350815230/test
docker run --name super_pay --add-host super-os:172.17.0.1 -d -p 8080:8080 1350815230/test:latest  catalina.sh run

docker login --username=godword@foxmail.com registry.cn-hongkong.aliyuncs.com
docker tag test:latest registry.cn-hongkong.aliyuncs.com/ersonw/test:latest
docker push registry.cn-hongkong.aliyuncs.com/ersonw/test:latest


docker login --username=godword@foxmail.com registry.cn-hongkong.aliyuncs.com
docker pull registry.cn-hongkong.aliyuncs.com/ersonw/test


docker run --name super_pay --add-host super-os:172.22.46.191 -d -p 8080:8080 registry.cn-hongkong.aliyuncs.com/ersonw/test  catalina.sh run
