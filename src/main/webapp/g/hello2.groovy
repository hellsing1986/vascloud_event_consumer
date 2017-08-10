import com.codahale.metrics.SharedMetricRegistries
import com.codahale.metrics.Meter
import com.codahale.metrics.Counter
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse

ServletRequest req = request
ServletResponse res = response
def retBody = """hello world! (2)"""

res.contentType = "text/plain; charset=utf-8"

out.write retBody
out.flush()