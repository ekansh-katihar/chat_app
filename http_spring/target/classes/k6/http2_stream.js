//docker run --network code_hz-network --rm -i grafana/k6 run - <script.js 

import http from 'k6/http';
import { sleep, check } from 'k6';

export const options = {
  // A number specifying the number of VUs to run concurrently.
  vus: 100    ,
  // A string specifying the total duration of the test run.
  duration: '10s',
  insecureSkipTLSVerify: true,
};
let url = `${__ENV.HOST}`


 

// "/trades/timestamps",https://localhost:8080/api/stock_streaming_response_body
function streamTradesBetween(){
  let res = http.get(url+'api/stock_stream_db', { 
      headers: { accept: 'text/event-stream' },
      timeout: 60000 
  });
  // console.log(JSON.stringify(res));
  check(res, {
      'Get Trade between Timstamp endpoint status is 200': (res) => res.status === 200,
  }); 
}

 

// The function that defines VU logic.
//
// See https://grafana.com/docs/k6/latest/examples/get-started-with-k6/ to learn more
// about authoring k6 scripts.
//
export default function() {

  streamTradesBetween();
   
  
}
