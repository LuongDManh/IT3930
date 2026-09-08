#!/bin/bash

echo "=========================================="
echo "  CREATE DOCKER CONFIGS  "
echo "=========================================="
docker config ls -q | xargs docker config rm
# Tạo Prometheus config
if docker config ls | grep -q "prometheus_config"; then
    echo "🔄 Prometheus config đã tồn tại, đang xóa..."
    docker config rm prometheus_config
fi
echo "✅ Tạo Prometheus config..."
docker config create prometheus_config ./monitoring/prometheus/prometheus.yml

# Tạo Grafana datasources config
if docker config ls | grep -q "grafana_datasources"; then
    echo "🔄 Grafana datasources config đã tồn tại, đang xóa..."
    docker config rm grafana_datasources
fi
echo "✅ Tạo Grafana datasources config..."
docker config create grafana_datasources ./monitoring/grafana/provisioning/datasources/prometheus.yml

# Tạo Grafana dashboards config
if docker config ls | grep -q "grafana_dashboards"; then
    echo "🔄 Grafana dashboards config đã tồn tại, đang xóa..."
    docker config rm grafana_dashboards
fi
echo "✅ Tạo Grafana dashboards config..."
docker config create grafana_dashboards ./monitoring/grafana/provisioning/dashboards/dashboards.yml

echo ""
echo "=========================================="
echo "  DANH SÁCH CONFIGS  "
echo "=========================================="
docker config ls
