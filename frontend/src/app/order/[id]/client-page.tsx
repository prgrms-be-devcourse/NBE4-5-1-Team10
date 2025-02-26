import { Card } from "@/components/ui/card";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { components } from "@/lib/backend/generated/schema";
import { CheckCircle } from "lucide-react";

const statusIndex = {
  ORDERED: 0, // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: 1, // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: 1, // 익일 배송 준비 → 주황색
  SHIPPED: 2, // 배송 출발 → 보라색
  DELIVERED: 3, // 배송 완료 → 회색
};

const statusString = {
  ORDERED: "주문 완료", // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: "배송 준비", // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: "배송 준비", // 익일 배송 준비 → 주황색
  SHIPPED: "배송 출발", // 배송 출발 → 보라색
  DELIVERED: "배송 완료", // 배송 완료 → 회색
};

export default function ClientPage({
  order,
}: {
  order: components["schemas"]["OrderDetailResponse"];
}) {
  return (
    <div className="flex justify-center items-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-lg p-8 bg-white shadow-md">
        <h2 className="text-2xl font-semibold text-center mb-8">
          주문 상세 정보
        </h2>

        <Table className="mb-8">
          <TableHeader>
            <TableRow className="bg-gray-100">
              <TableHead className="text-gray-700 font-semibold pl-4">
                상품명
              </TableHead>
              <TableHead className="text-gray-700 font-semibold text-center">
                수량
              </TableHead>
              <TableHead className="text-gray-700 font-semibold text-right pr-4">
                가격
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {order.orderItems.map((item, index) => (
              <TableRow key={index}>
                <TableCell className="pl-4">{item.productName}</TableCell>
                <TableCell className="text-center">
                  {item.quantity} 개
                </TableCell>
                <TableCell className="text-right pr-4">
                  ₩{item.price?.toLocaleString()}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>

        <div className="mt-12">
          <h3 className="text-lg font-semibold mb-6">배송 상태</h3>
          <div className="flex items-center w-full">
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 0
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 0
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                주문 완료
              </span>
            </div>
            <div
              className={`w-1/4 h-1 ${
                statusIndex[order.orderStatus] >= 1
                  ? "bg-green-500"
                  : "bg-gray-300"
              }`}
            ></div>
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 1
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 1
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                배송 준비
              </span>
            </div>
            <div
              className={`w-1/4 h-1 ${
                statusIndex[order.orderStatus] >= 2
                  ? "bg-green-500"
                  : "bg-gray-300"
              }`}
            ></div>
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 2
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 2
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                배송 출발
              </span>
            </div>
            <div
              className={`w-1/4 h-1 ${
                statusIndex[order.orderStatus] >= 3
                  ? "bg-green-500"
                  : "bg-gray-300"
              }`}
            ></div>
            <div className="flex flex-col items-center w-1/4">
              <CheckCircle
                className={`w-8 h-8 ${
                  statusIndex[order.orderStatus] >= 3
                    ? "text-green-500"
                    : "text-gray-300"
                }`}
              />
              <span
                className={`mt-2 text-sm font-medium ${
                  statusIndex[order.orderStatus] >= 3
                    ? "text-green-600"
                    : "text-gray-700"
                }`}
              >
                배송 완료
              </span>
            </div>
          </div>
        </div>

        {/* 배송 정보 & 결제 정보 */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mt-10">
          <div>
            <h3 className="text-lg font-semibold mb-2">배송 정보</h3>
            <p className="text-gray-600">수령인: {order.username}</p>
            <p className="text-gray-600">주소: {order.address}</p>
            <p className="text-gray-600">우편번호: {order.postalCode}</p>
          </div>

          <div>
            <h3 className="text-lg font-semibold mb-2">결제 정보</h3>
            <p className="text-gray-800 font-semibold">
              총 결제금액: ₩{order.totalPrice.toLocaleString()}
            </p>
          </div>
        </div>
      </Card>
    </div>
  );
}
