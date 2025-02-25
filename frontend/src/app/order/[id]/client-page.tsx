import { Card } from "@/components/ui/card";
import { components } from "@/lib/backend/generated/schema";

export default function ClientPage({
  order,
}: {
  order: components["schemas"]["OrderDetailResponse"];
}) {

    

    return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-lg p-8 bg-white shadow-md">
        <h2 className="text-2xl font-semibold mb-6 text-center">주문 상세 정보</h2>

        {/* 주문 상품 목록 */}
        <table className="w-full border-collapse border border-gray-200">
          <thead>
            <tr className="bg-gray-100 text-left">
              <th className="border p-3">상품명</th>
              <th className="border p-3 text-center">수량</th>
              <th className="border p-3 text-right">가격</th>
            </tr>
          </thead>
          <tbody>
            {order.orderItems?.map((item) => (
              <tr key={item.productName} className="border">
                <td className="border p-3">{item.productName}</td>
                <td className="border p-3 text-center">{item.quantity}</td>
                <td className="border p-3 text-right">
                  {item.price?.toLocaleString()}원
                </td>
              </tr>
            ))}
          </tbody>
        </table>

        

        {/* 배송 정보 & 결제 정보 */}
        <div className="mt-8 grid grid-cols-1 md:grid-cols-2 gap-6">
          <Card className="p-6 bg-gray-50">
            <h3 className="font-semibold mb-4">배송 정보</h3>
            <p>이메일: {order.email}</p>
            <p>주소: {order.address}</p>
            <p>우편번호: {order.postalCode}</p>
          </Card>

          <Card className="p-6 bg-gray-50">
            <h3 className="font-semibold mb-4">결제 정보</h3>
            <p className="font-semibold">
              총 결제금액: {order.totalPrice?.toLocaleString()}원
            </p>
          </Card>
        </div>
      </Card>
    </div>
  )};