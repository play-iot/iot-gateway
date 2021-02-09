import http from 'k6/http';
import { check, sleep } from 'k6';

export default function() {
  let res = http.get('http://zpi:8888/api/discover/bacnet/network');
  check(res, { 'status was 200': (r) => r.status === 200 });
  sleep(1);
}
