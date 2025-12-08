import http from 'k6/http';
import { check } from 'k6';
import { Trend } from 'k6/metrics';

// ✅ 10,000명의 유저가 각각 1회 요청
export const options = {
  scenarios: {
    coupon_request_10k: {
      executor: 'per-vu-iterations',
      vus: 1000,
      iterations: 2,     // 각 VU는 1회만 실행
      maxDuration: '60s' // 전체 테스트 최대 60초
    },
  },
};

const BASE_URL = 'http://localhost:8000';
const COUPON_ID = __ENV.COUPON_ID || 'test-coupon';

// Authorization 헤더용 토큰 (환경변수 우선)
const AUTH_TOKEN =
    __ENV.AUTH_TOKEN ||
    'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI4MWNkMTIwZC0zZDQzLTQyZTgtYjQ0Yy01MzU4ZDgwYTNiOWYiLCJpYXQiOjE3NjQ4NDE5MzEsImV4cCI6MTc5NTY4NjczMX0.Us9QUxr5c8Rt5LNBRnkmFGHKC_01tACLfhVl3Sze-6M';

// 성공 응답 전용 duration 메트릭
const successDuration = new Trend('successful_req_duration');

export default function () {
  // 🔹 각 VU별 고유 userId (1명 = 1 userId)
  const userId = `user-${__VU}`;

  const url = `${BASE_URL}/coupon-service/v1/${COUPON_ID}/request`;

  const params = {
    headers: {
      'Content-Type': 'text/plain',
      Authorization: `Bearer ${AUTH_TOKEN}`,
    },
  };

  const res = http.post(url, userId, params);

  const isOk = res.status === 200;

  check(res, {
    'status is 200': () => isOk,
  });

  if (isOk) {
    successDuration.add(res.timings.duration);
  }
}
