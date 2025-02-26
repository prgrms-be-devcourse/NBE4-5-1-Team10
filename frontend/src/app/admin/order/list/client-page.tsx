"use client";

import { Card } from "@/components/ui/card";
import dayjs from "dayjs";
import {
  Table,
  TableHeader,
  TableRow,
  TableHead,
  TableBody,
  TableCell,
} from "@/components/ui/table";
import Image from "next/image";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { useRouter } from "next/navigation";
import { components } from "@/lib/backend/generated/schema";
import { useEffect, useState } from "react";
import Link from "next/link";

const statusString = {
  ORDERED: "주문 완료", // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: "당일 배송 준비 완료", // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: "익일 백송 준비 완료", // 익일 배송 준비 → 주황색
  SHIPPED: "배송 출발", // 배송 출발 → 보라색
  DELIVERED: "배송 완료", // 배송 완료 → 회색
};

const statusColors = {
  ORDERED: "bg-blue-100 text-blue-700", // 주문 완료 → 파란색
  READY_DELIVERY_SAME_DAY: "bg-yellow-100 text-yellow-700", // 당일 배송 준비 → 노란색
  READY_DELIVERY_NEXT_DAY: "bg-orange-100 text-orange-700", // 익일 배송 준비 → 주황색
  SHIPPED: "bg-purple-100 text-purple-700", // 배송 출발 → 보라색
  DELIVERED: "bg-gray-100 text-gray-700", // 배송 완료 → 회색
};

export default function ClientPage() {
  const router = useRouter();
  const [orders, setOrders] = useState<
    components["schemas"]["OrderDetailListResponse"][]
  >([]);

  async function initFetchOrders() {
    try {
      const res = await fetch("/api/admin/orders", {
        method: "GET",
      });
      if (!res.ok) {
        console.error("Fetch data failed:", res.status);
        return;
      }
      const { data } = await res.json();
      const orders =
        data.items as components["schemas"]["OrderDetailListResponse"][];
      setOrders(orders);
    } catch (err) {
      console.error("주문 목록을 가져오는 데에 실패했습니다.", err);
    }
  }

  useEffect(() => {
    initFetchOrders();
  }, []);

  return (
    <div className="flex justify-center w-full min-h-screen px-8 py-12 bg-gray-100">
      <Card className="w-full max-w-screen-lg p-6 bg-white shadow-md">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-lg font-semibold">주문 목록</h3>
        </div>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>주문번호</TableHead>
              <TableHead>주문시각</TableHead>
              <TableHead>고객명</TableHead>
              <TableHead>주문 상품</TableHead>
              <TableHead>주문 상태</TableHead>
              <TableHead className="text-right whitespace-nowrap">
                결제 금액
              </TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {orders.map((order) => (
              <Link
                key={order.orderId}
                href={`/admin/order/${order.orderId}`}
                legacyBehavior
                passHref
              >
                <TableRow
                  className="cursor-pointer hover:bg-gray-100 transition-colors"
                  key={order.orderId}
                  onClick={() => router.push(`/admin/order/${order.orderId}`)}
                >
                  <TableCell># {order.orderId}</TableCell>
                  <TableCell>
                    {dayjs(order.orderDate).format(
                      "YYYY년 MM월 DD일 HH시 mm분"
                    )}
                  </TableCell>
                  <TableCell>{order.username}</TableCell>
                  <TableCell>
                    <div className="flex items-center gap-4">
                      <Image
                        src={order.firstProductImageUrl || ""}
                        alt={order.firstProductName}
                        width={30}
                        height={30}
                        className="rounded-md"
                        style={{ width: 30, height: 30 }}
                      />
                      <p>
                        {order.firstProductName}
                        {order.productCategoryCount > 1 &&
                          ` 외 ${order.productCategoryCount - 1}건`}
                      </p>
                    </div>
                  </TableCell>
                  <TableCell>
                    <Badge className={statusColors[order.orderStatus]}>
                      {statusString[order.orderStatus]}
                    </Badge>
                  </TableCell>
                  <TableCell className="text-right whitespace-nowrap font-semibold">
                    {order.totalPrice.toLocaleString()}원
                  </TableCell>
                </TableRow>
              </Link>
            ))}
          </TableBody>
        </Table>
      </Card>
    </div>
  );
}
