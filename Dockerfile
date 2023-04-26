FROM alsadi/containerized-xorg-spice

USER root

RUN curl https://download.java.net/java/GA/jdk19.0.2/fdb695a9d9064ad6b064dc6df578380c/7/GPL/openjdk-19.0.2_linux-x64_bin.tar.gz --output java.tar.gz \
&& tar xvf java.tar.gz \
&& rm java.tar.gz \
&& rm /home/app/.config/openbox/autostart.d/* \
&& sh -c 'echo "/jdk-19.0.2/bin/java -jar /app.jar" > /home/app/.config/openbox/autostart.d/prodam_garage.sh' \
&& chmod +x /home/app/.config/openbox/autostart.d/prodam_garage.sh \
&& dnf install -y mesa-dri-drivers \
&& dnf install -y gtk3 \
&& dnf clean all \
&& rm -rf /var/cache/dnf

COPY ${JAR_FILE} /app.jar

EXPOSE 5900

USER app
