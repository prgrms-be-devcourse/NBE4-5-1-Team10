import ClientPage from "./client-page";
import client from "@/lib/backend/client";

export default async function Page({
  params,
}: {
  params: {
    id: number;
  };
}) {
  const { id } = await params;

  const response = await client.GET("/api/v1/products/{productId}", {
    params: {
      path: {
        productId: id,
      },
    },
  });

  if (response.error) {
    return <div>{response["error"]["msg"]}</div>;
  }

  const data = response.data!!;
  const product = data.data!!;

  //TODO(2hwayoung): fix to real data
  const reviews = [
    {
      id: 1,
      user: "김**",
      rating: 5,
      comment: "고소하고 맛있어요.",
      date: "2025.01.15",
    },
    {
      id: 2,
      user: "이**",
      rating: 4,
      comment: "양이 넉넉하고 온 가족이 같이 즐길 수 있어요.",
      date: "2025.01.14",
    },
  ];

  return <ClientPage product={product} reviews={reviews} />;
}
